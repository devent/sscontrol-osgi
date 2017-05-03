package com.anrisoftware.sscontrol.k8s.fromreposiroty.internal.script_1_5

import static com.anrisoftware.globalpom.utils.TestUtils.*

import java.nio.charset.StandardCharsets

import javax.inject.Inject

import org.apache.commons.io.FileUtils
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

import com.google.inject.Guice
import com.google.inject.Injector

class StgFileTemplateTest {

    @Inject
    StgFileTemplate fileTemplate

    @Rule
    public TemporaryFolder folder = new TemporaryFolder()

    @Test
    void "file yaml"() {
        def args = [vars: [wordpress: [version: '1.0']], parent: this]
        def encoding = StandardCharsets.UTF_8
        def file = folder.newFile('wordpress.yaml.stg')
        FileUtils.write file, '''
wordpress_yaml(parent, vars) ::= <<
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
        def string = fileTemplate.parseFile file, args, encoding
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
