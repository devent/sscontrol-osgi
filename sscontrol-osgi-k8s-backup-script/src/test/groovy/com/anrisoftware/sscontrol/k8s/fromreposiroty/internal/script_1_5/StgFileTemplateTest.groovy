package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*

import java.nio.charset.StandardCharsets

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.anrisoftware.sscontrol.k8s.backup.service.internal.script_1_5.FileTemplateModule
import com.anrisoftware.sscontrol.k8s.backup.service.internal.script_1_5.StgFileTemplateParser
import com.google.inject.Guice
import com.google.inject.Injector

import groovy.util.logging.Slf4j

/**
 * 
 *
 * @author Erwin Müller <erwin.mueller@deventm.de>
 * @version 1.0
 */
@Slf4j
class StgFileTemplateTest {

    @Inject
    StgFileTemplateParser templateParser

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Test
    void "file name yaml"() {
        [
            [
                file: 'wordpress-yaml.stg',
                expected: 'wordpress.yaml'
            ],
            [
                file: 'wordpress-aaa-yaml.stg',
                expected: 'wordpress-aaa.yaml'
            ],
        ].each { Map test ->
            log.info 'Test: {}', test
            def string = templateParser.getFilename new File(test.file)
            assertStringContent string, test.expected
        }
    }

    @Test
    void "file yaml"() {
        def args = [vars: [wordpress: [version: '1.0']], parent: this]
        def encoding = StandardCharsets.UTF_8
        def dir = folder.newFolder()
        createTemplates dir
        def string = templateParser.parseFile dir, 'wordpress-yaml.stg', args, encoding
        assertStringContent string, '''\
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: wordpress
  labels:
    app: wordpress
spec:
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: wordpress
        tier: frontend
    spec:
      containers:
      - image: wordpress:1.0
        name: wordpress
        env:
        - name: WORDPRESS_DB_HOST
          value: wordpress-mysql
        - name: WORDPRESS_DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-pass
              key: password.txt
        ports:
        - containerPort: 80
          name: wordpress
        volumeMounts:
        - name: wordpress-persistent-storage
          mountPath: /var/www/html
      volumes:
      - name: wordpress-persistent-storage
        persistentVolumeClaim:
          claimName: wp-pv-claim
'''
    }

    def createTemplates(File dir) {
        FileUtils.write new File(dir, 'wordpress-yaml.stg'), '''
wordpress-yaml(parent, vars) ::= <<
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: wordpress
  labels:
    app: wordpress
spec:
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: wordpress
        tier: frontend
    spec:
      containers:
      - image: wordpress:<vars.wordpress.version>
        name: wordpress
        env:
        - name: WORDPRESS_DB_HOST
          value: wordpress-mysql
        - name: WORDPRESS_DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-pass
              key: password.txt
        ports:
        - containerPort: 80
          name: wordpress
        volumeMounts:
        - name: wordpress-persistent-storage
          mountPath: /var/www/html
      volumes:
      - name: wordpress-persistent-storage
        persistentVolumeClaim:
          claimName: wp-pv-claim

>>
''', StandardCharsets.UTF_8
        FileUtils.write new File(dir, 'mysql-yaml.stg'), '''
mysql-yaml(parent, vars) ::= <<
apiVersion: v1
kind: Service
metadata:
  name: wordpress-mysql
  labels:
    app: wordpress
spec:
  ports:
    - port: 3306
  selector:
    app: wordpress
    tier: mysql
  clusterIP: None
---
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: wordpress-mysql
  labels:
    app: wordpress
spec:
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: wordpress
        tier: mysql
    spec:
      containers:
      - image: mysql:5.6
        name: mysql
        env:
          # $ kubectl create secret generic mysql-pass --from-file=password.txt
          # make sure password.txt does not have a trailing newline
        - name: MYSQL_ROOT_PASSWORD
          valueFrom:
            secretKeyRef:
              name: mysql-pass
              key: password.txt
        ports:
        - containerPort: 3306
          name: mysql
        volumeMounts:
        - name: mysql-persistent-storage
          mountPath: /var/lib/mysql
      volumes:
      - name: mysql-persistent-storage
        persistentVolumeClaim:
          claimName: mysql-pv-claim

>>
''', StandardCharsets.UTF_8
    }

    Injector injector

    @Before
    void setupTest() {
        toStringStyle
        this.injector = createInjector()
        this.injector.injectMembers(this)
    }

    Injector createInjector() {
        Guice.createInjector(
                new FileTemplateModule()
                )
    }
}
