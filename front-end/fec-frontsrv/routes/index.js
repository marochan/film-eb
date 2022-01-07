import express from "express";
import { OAuth2Client } from "google-auth-library";
import got from "got";
import eureka from "../bin/eureka-helper.js";

const CLIENT_ID =
  "405815393608-333ae8if6up2dn84je64ph76d5c211d2.apps.googleusercontent.com";
const client = new OAuth2Client(CLIENT_ID);
const router = express.Router();

/**
 * @param {string} name - App ID
 * @returns App instance
 */
function getEurekaInstance(name) {
  const instances = eureka.client.getInstancesByAppId(name);
  if (instances.length) {
    return instances[0];
  } else {
    return null;
  }
}

/**
 * @param {EurekaClient.EurekaInstanceConfig} config - Instance configuration from eureka
 * @returns URL as string
 */
function getUrlFromInstance(config) {
  return `http://${config.hostName}:${config.port.$}`;
}

function getUrl(id) {
  let config = getEurekaInstance(id);
  if (!config) {
    throw new Error(`No active instances of ID: ${id}`);
  }
  return getUrlFromInstance(config);
}

/* GET home page. */
router.get("/", (req, res, next) => {
  res.render("index", { title: "Express" });
});

router.get("/search", async (req, res, next) => {
  try {
    const searchParams = new URL(req.url, `http://${req.headers.host}`)
      .searchParams;

    const redir = await got.get(getUrl("rating-management"), {
      searchParams,
    });

    res.status(redir.statusCode).json(redir.body);
  } catch (e) {
    console.log(e);
    res.status(500).send(e);
  }
});

// Verify token
router.use(async (req, res, next) => {
  let token = req.header("Authorization");

  if (!token) {
    res.status(400).send("Invalid token");
    return;
  }

  token = token.slice(7);
  console.log("Token verification");

  const ticket = await client.verifyIdToken({
    idToken: token,
    audience: CLIENT_ID,
  });

  const payload = ticket.getPayload();
  const userId = ticket.getUserId();
  // console.log(userId, payload);

  res.locals.auth = {
    userId: userId,
    nickname: payload.name,
  };

  next();
});

router.post("/join", async (req, res, next) => {
  console.log("join", res.locals.auth.nickname);
  try {
    const url = new URL("/create-account", getUrl("user-service")).toString();
    const redir = await got.post(url, {
      json: res.locals.auth,
    });

    res.status(redir.statusCode).json(redir.body);
  } catch (e) {
    console.log(e);
    res.status(500).send(e);
  }
});

router.get("/recommended", async (req, res, next) => {
  console.log("recommended", res.locals.auth.nickname);
  try {
    const url = new URL(
      "/checkrecommended",
      getUrl("recommendation-processing")
    ).toString();
    const redir = await got.post(url, {
      json: {
        userId: res.locals.auth.userId,
      },
    });

    res.status(redir.statusCode).json(redir.body);
  } catch (e) {
    console.log(e);
    res.status(500).send(e);
  }
});

export default router;
