@echo off
cd /d "%~dp0demo"
call npm install
call npx tsx demo.ts
pause