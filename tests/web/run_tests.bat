@echo off
REM ==========================================
REM Script de Execução Automatizada - RadarInvest
REM ==========================================

echo [INFO] Iniciando testes automatizados...
call robot -d logs specs

echo.
echo [INFO] Gerando relatorio simplificado (TXT)...
python generate_report.py

echo.
echo [INFO] Concluido! Verifique a pasta 'logs'.
pause
