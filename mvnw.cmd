@echo off
setlocal
set "MAVEN_VERSION=3.9.9"
set "DIST_NAME=apache-maven-%MAVEN_VERSION%"
set "DIST_ROOT=%USERPROFILE%\.m2\wrapper\dists"
set "MAVEN_HOME=%DIST_ROOT%\%DIST_NAME%"
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $name='%DIST_NAME%'; $url='https://archive.apache.org/dist/maven/maven-3/%MAVEN_VERSION%/binaries/'+$name+'-bin.zip'; $root='%DIST_ROOT%'; New-Item -ItemType Directory -Force -Path $root | Out-Null; $zip=Join-Path $env:TEMP ($name+'-bin.zip'); Invoke-WebRequest -UseBasicParsing $url -OutFile $zip; $expected=((Invoke-WebRequest -UseBasicParsing ($url+'.sha512')).Content -split '\s+')[0].ToLowerInvariant(); $actual=(Get-FileHash -Algorithm SHA512 $zip).Hash.ToLowerInvariant(); if ($actual -ne $expected) { throw 'Maven archive SHA-512 verification failed' }; Expand-Archive -Force $zip $root; Remove-Item $zip"
  if errorlevel 1 exit /b 1
)
call "%MAVEN_HOME%\bin\mvn.cmd" %*
exit /b %ERRORLEVEL%