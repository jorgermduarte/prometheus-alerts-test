@echo off
setlocal

rem
set RECEIVER=discord-webhook
set ALERTNAME=TestAlert
set SEVERITY=warning
set SUMMARY=Alerta de teste
set DESCRIPTION=Este é um alerta de teste para o Discord via webhook.

rem
echo [{"labels":{"alertname":"%ALERTNAME%","severity":"%SEVERITY%"},"annotations":{"summary":"%SUMMARY%","description":"%DESCRIPTION%"}}] > alert.json

rem
curl -X POST -d @alert.json -H "Content-Type: application/json" http://localhost:9093/api/v1/alerts

rem
del alert.json

endlocal
