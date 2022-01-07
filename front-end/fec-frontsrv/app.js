import express from "express";
import path from "path";
import cookieParser from "cookie-parser";
import morgan from "morgan";
import cors from "cors";

const logger = morgan;

import indexRouter from "./routes/index.js";
import { fileURLToPath } from "url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));
const app = express();

app.use(cors());
app.use(logger("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, "public")));

app.use("/", indexRouter);

export default app;
