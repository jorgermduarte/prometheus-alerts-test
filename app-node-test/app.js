const express = require('express');
const promClient = require('prom-client');

const app = express();
const PORT = 80;

const prometheusRegister = promClient.register;
const httpRequestDurationMicroseconds = new promClient.Histogram({
  name: 'http_request_duration_ms',
  help: 'Duration of HTTP requests in ms',
  labelNames: ['method', 'route', 'status'],
  buckets: [0.1, 5, 15, 50, 100, 500],
});
const httpRequestsTotal = new promClient.Counter({
  name: 'http_requests_total',
  help: 'Total number of HTTP requests',
  labelNames: ['method', 'route', 'status'],
});

app.use((req, res, next) => {
  const start = Date.now();
  res.on('finish', () => {
    const end = Date.now();
    const responseTimeInMs = end - start;

    httpRequestDurationMicroseconds
      .labels(req.method, req.originalUrl, res.statusCode)
      .observe(responseTimeInMs);

    httpRequestsTotal
      .labels(req.method, req.originalUrl, res.statusCode)
      .inc();
  });
  next();
});

app.get('/', (req, res) => {
  res.status(200).send('Hello, World!');
});

app.get('/metrics', async (req, res) => {
  try {
    res.set('Content-Type', prometheusRegister.contentType);
    const metrics = await prometheusRegister.metrics();
    res.send(metrics);
  } catch (error) {
    console.error('Erro ao obter métricas do Prometheus:', error);
    res.status(500).send('Erro ao obter métricas do Prometheus');
  }
});


app.listen(PORT, () => {
  console.log(`Started on port: ${PORT}`);
});
