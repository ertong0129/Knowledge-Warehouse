server版本：3.13.0.11

wget https://www.aerospike.com/artifacts/aerospike-server-community/3.13.0.11/aerospike-server-community-3.13.0.11-el7.tgz --no-check-certificate

tar -xvf aerospike-server-community-3.13.0.11-el7.tgz

aerospike-server-community-3.13.0.11-el7/asinstall

启动：systemctl start aerospike

配置文件：/etc/aerospike/aerospike.conf



tool版本：3.15.3.6

wget https://www.aerospike.com/artifacts/aerospike-tools/3.15.3.6/aerospike-tools-3.15.3.6-el7.tgz --no-check-certificate



amc版本：3.6.13

wget https://artifacts.aerospike.com/aerospike-amc-community/3.6.13/aerospike-amc-community-3.6.13.tar.gz --no-check-certificate

tar -zxvf aerospike-amc-community-3.6.13.tar.gz

yum install gcc python-devel

aerospike-amc-community-3.6.13/install

启动：/etc/init.d/amc start

配置文件：/etc/amc/config/gunicorn_config.py



daily环境单机配置文件

```json
service {
	paxos-single-replica-limit 1 # Number of nodes where the replica count is automatically reduced to 1.
	proto-fd-max 15000
}

logging {
	file /data/aerospike/logs/aerospike.log {
		context any info
	}
}

network {
	service {
		address any
		port 3000
	}

	heartbeat {
		mode multicast
        multicast-group 239.1.99.222
        port 9918

        # To use unicast-mesh heartbeats, remove the 3 lines above, and see
        # aerospike_mesh.conf for alternative.

		interval 150
		timeout 10
	}

	fabric {
		port 3001
	}

	info {
		port 3003
	}
}

namespace ns1 {
  replication-factor 1
  memory-size 4G
  default-ttl 30d # 30 days, use 0 to never expire/evict.
  #storage-engine memory
  storage-engine device {         
          file /data/aerospike/namespace/ns1/data1.dat   
          filesize 128G
  				#所有数据都存内存一份（内存满了会触发淘汰机制，当硬盘比内存大很多的时候，不建议使用）
          #data-in-memory true            
  }
}

namespace ns2 {
  replication-factor 1
  memory-size 4G
  default-ttl 30d # 30 days, use 0 to never expire/evict.
  #storage-engine memory
  storage-engine device {         
          file /data/aerospike/namespace/ns2/data1.dat   
          filesize 128G               
  				#所有数据都存内存一份（内存满了会触发淘汰机制，当硬盘比内存大很多的时候，不建议使用）
          #data-in-memory true            
  }
}
```



参考地址：

https://note.youdao.com/ynoteshare/index.html?id=d8092bdd883e907ad1e62ba81b2317dd&type=note&_time=1639532271324

https://www.jianshu.com/p/2ef92945a2c4

https://docs.aerospike.com/docs/reference/configuration/

https://blog.csdn.net/songhuiqiao/article/details/50262647

https://www.cnblogs.com/xiaoit/p/4554607.html