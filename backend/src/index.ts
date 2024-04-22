import express from 'express';

const app = express();
const port = 3001; // Port par défaut

app.get('/', (req, res) => {
  res.send('Hello World avec TypeScript!');
});

app.listen(port, () => {
  console.log(`Serveur lancé sur http://localhost:${port}`);
});
