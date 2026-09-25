@echo off
cd /d "%~dp0engine\lecomptoir"
mvn clean compile exec:java -Dexec.mainClass="com.andrenicolas.App"
