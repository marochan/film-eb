import { Eureka } from "eureka-js-client";

const eurekaHost = process.env.EUREKA_HOST || "runtime-config";
const eurekaPort = process.env.EUREKA_PORT || 8761;
const hostName = process.env.HOSTNAME || "localhost";
const ipAddr = process.env.IP_ADDRESS || "172.0.0.1";

export function registerWithEureka(appName, PORT) {
  const client = new Eureka({
    instance: {
      app: appName,
      hostName: hostName,
      ipAddr: ipAddr,
      port: {
        $: PORT,
        "@enabled": true,
      },
      vipAddress: appName,
      dataCenterInfo: {
        "@class": "com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo",
        name: "MyOwn",
      },
    },
    eureka: {
      host: eurekaHost,
      port: eurekaPort,
    },
  });

  // client.logger.level("debug");

  client.start((error) => {
    console.log(error || "service registered");
  });

  function exitHandler(options, exitCode) {
    if (options.cleanup) {
    }
    if (exitCode || exitCode === 0) console.log(exitCode);
    if (options.exit) {
      client.stop();
    }
    setTimeout(() => process.exit(1), 10000);
  }

  client.on("deregistered", () => {
    process.exit();
    console.log("after deregistered");
  });

  client.on("started", () => {
    console.log("eureka host  " + eurekaHost);
  });

  process.on("SIGINT", exitHandler.bind(null, { exit: true }));
  return client;
}

export function init(name, port) {
  _client = registerWithEureka(name, port);
}

/** @type {Eureka} */
let _client = null;

export default {
  get client() {
    return _client;
  },
};
