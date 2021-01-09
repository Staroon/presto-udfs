### Presto SQL UDFS
Presto SQL 自定义函数    

### 项目环境
- Presto SQL: 340
- Java: 11.0.9 (zulu11.43.55-ca-jdk11.0.9.1)
- Maven: 3.6.3

### 函数列表
#### 标量函数（Scalar Function）
- **ip_location**(mode, ip) -> varchar    
    - 函数描述    
    IP 定位函数，依赖于网络开源的 IP 数据库，如无必要，不会频繁更新，预计每半年更新一次，如果查询返回值为 0 ，说明 IP 位置信息未收录
    - 参数说明    
        - mode，查询模式，字符串类型，支持参数：country（国家）、province（省份）、city（城市）、isp（网络运营商）    
        - ip，IP 地址，字符串类型，格式如：xxx.xxx.xxx.xxx    
    - 使用示例
    ```sql
    select 
      ip_location('country','112.126.60.145') "国家",
      ip_location('province','112.126.60.145') "省份",
      ip_location('city','112.126.60.145') "城市",
      ip_location('isp','112.126.60.145') "网络运营商",
      ip_location('city','112.'), -- 非正常 IP ，返回空白字符串
      ip_location('city',null), -- IP 值为 null ，返回空白字符串
      ip_location('city','36.98.202.245') -- 返回值为 0 ，说明未收录该 IP 该项位置信息
    ;
    ```

### 使用说明
1. 克隆项目到本地，`git clone project`    
2. 编译项目，`mvn clean package`    
3. 得到插件 zip 包：`presto-udfs-340.zip`    
4. 将`data`目录下的`ip2region.db`文件放入编译得到的`presto-udfs-340.zip`压缩包中    
5. 上传`presto-udfs-340.zip`至Presto服务器，解压 zip 文件至 Presto SQL 安装目录 **plugin** 目录下    
6. 重启 Presto SQL    