const express = require("express");
const cors = require("cors");
const fs = require("fs");
const path = require("path");

const app = express();
const PORT = 3000;
const leaderboardFile = path.join(__dirname, "leaderboard.json");

app.use(cors());
app.use(express.json());

const WORDS_BY_LEVEL = {
  1: ["cat", "book", "game", "java", "note"],
  2: ["planet", "mobile", "random", "puzzle", "coding"],
  3: ["retrofit", "variable", "activity", "fragment", "database"]
};

function randomFrom(arr) {
  return arr[Math.floor(Math.random() * arr.length)];
}

function loadLeaderboard() {
  try {
    return JSON.parse(fs.readFileSync(leaderboardFile, "utf8"));
  } catch {
    return [];
  }
}

function saveLeaderboard(data) {
  fs.writeFileSync(leaderboardFile, JSON.stringify(data, null, 2));
}

app.get("/random", (req, res) => {
  const level = Number(req.query.level || 1);
  const words = WORDS_BY_LEVEL[level] || WORDS_BY_LEVEL[3];
  res.json({ word: randomFrom(words) });
});

app.get("/tip", (req, res) => {
  const word = (req.query.word || "").toLowerCase();
  if (!word) return res.json({ tip: "No tip available" });

  // Simple tip: first + last letter
  const tip = `Starts with '${word[0]}' and ends with '${word[word.length - 1]}'`;
  res.json({ tip });
});

app.post("/leaderboard", (req, res) => {
  const entry = req.body;
  if (!entry || !entry.name) return res.status(400).json({ error: "Invalid entry" });

  const data = loadLeaderboard();
  data.push(entry);
  saveLeaderboard(data);
  res.status(201).json({ ok: true });
});

app.get("/leaderboard", (req, res) => {
  const data = loadLeaderboard();
  data.sort((a, b) => b.score - a.score || a.timeSeconds - b.timeSeconds);
  res.json(data.slice(0, 50));
});

app.listen(PORT, () => {
  console.log(`wordguess backend running on http://localhost:${PORT}`);
});