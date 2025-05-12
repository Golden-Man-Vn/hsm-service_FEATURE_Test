REM -----------------------------------
REM -- prepare setting application

REM get bat directory
set "CURRENT_DIR=%~dp0"

REM move to current directory
cd /d "%CURRENT_DIR%"

REM display current directory
echo Now in: %CD%

REM ON/OFF DB migration by Flyway
REM set SPRING_FLYWAY_ENABLED=true
set SPRING_FLYWAY_ENABLED=false

REM perform copy application from build to RUN directory
copy ..\target\hsm-service-1.0.jar "%CURRENT_DIR%" /Y

REM -----------------------------------
REM -- application configuration

REM -- neon DB provider
REM set SPRING_DATASOURCE_URL=jdbc:postgresql://ep-jolly-waterfall-a4nys379-pooler.us-east-1.aws.neon.tech/neondb?sslmode=require
REM set SPRING_DATASOURCE_USERNAME=neondb_owner
REM set SPRING_DATASOURCE_PASSWORD=npg_r9BLuQZK5qhX

REM -- supabase DB provider
set SPRING_DATASOURCE_URL=jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres
set SPRING_DATASOURCE_USERNAME=postgres.uialljhfsnwxffnilhcq
set SPRING_DATASOURCE_PASSWORD=hytTgfr@$6f@DS

REM -----------------------------------
REM -- run application

REM assume compatible java placed in system directory
java -jar hsm-service-1.0.jar
