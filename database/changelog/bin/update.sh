#/bin/bash
cd ../
dirs=`ls`
cd bin
for i in $dirs; do
  if [ $i != "bin" ] && [ $i != "lib" ] && [ -d ../$i ]; then
    echo $i
    java -jar ../lib/liquibase.jar --defaultsFile=../$i/dev.properties --classpath="../lib/postgresql-9.1-903.jdbc4.jar;../lib/snakeyaml-1.13.jar" update
  fi
done
