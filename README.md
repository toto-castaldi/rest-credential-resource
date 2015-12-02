rest-credential-resource
===================

![logo](http://toto-castaldi.github.io/cdn/images/rest-credential-resource-logo.png "http://logomakr.com/0BKsDJ")

## Develop 

add on your jvm run :
```
-DMAIL_USERNAME="[ADDRESS]@gmail.com"
-DMAIL_PASSWORD="[GMAI-PASSWORD]"
-DMAIL_ADDRESS="[ADDRESS]@gmail.com"
-DRDS_USERNAME="[DB-USER]"
-DRDS_PASSWORD="[DB-PASSWORD]"
-DRDS_HOSTNAME="[DB-HOST]"
-DRDS_PORT="[DB-PORT]"
-DRDS_DB_NAME="[DB-NAME]"
```

### Aws Prod access
``` 
> ssh -i [PEM] [AWS-USER]@[AWS-IP] -L9999:[RDS-NAME]:5432
> psql -p 9999 -h localhost -W -U restCredentialRe ebdb
```

#### greatings

>Web graphic by <a href="http://www.freepik.com/">Freepik</a> from <a href="http://www.flaticon.com/">Flaticon</a> is licensed under <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a>. Made with <a href="http://logomakr.com" title="Logo Maker">Logo Maker</a>

> Written with [StackEdit](https://stackedit.io/).