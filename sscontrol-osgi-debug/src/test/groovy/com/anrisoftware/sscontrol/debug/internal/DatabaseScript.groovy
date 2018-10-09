package com.anrisoftware.sscontrol.debug.internal

def databasemap = [
    server: [host: '127.0.0.1', port: 3306],
    password: 'somepass',
    phpmyadmin: [
        name: 'phpmyadmin',
        password: 'somepass',
        db: 'phpmyadmindb',
    ],
    wordpressdb: [
        name: 'wordpressdb',
        password: 'somepass',
        db: 'wordpressdb',
    ],
]

database.with {
    bind databasemap.server.host, port: databasemap.server.port
    admin user: 'root', password: databasemap.password
    db name: databasemap.phpmyadmin.db
    user databasemap.phpmyadmin.name, password: databasemap.phpmyadmin.password with {
        access database: databasemap.phpmyadmin.db
    }
}

database.with {
    user databasemap.wordpressdb with {
        access database: databasemap.phpmyadmin.db
    }
}

database.with {
    debug "general", level: 1, file: "/var/log/mysql/mysql.log"
}

debugLogs = []
debugLogs << [name: "error", level: 1]
debugLogs << [name: "slow-queries", level: 1]

debugLogs.each { database.debug it }
