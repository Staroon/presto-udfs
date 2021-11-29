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
      IP 定位函数，依赖于开源的 IP 数据库项目[Ip2region](https://github.com/lionsoul2014/ip2region)，如果查询返回值为 0 ，说明 IP 位置信息未收录
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
      ip_location('city','112.'), -- 非正常 IP ，返回 null
      ip_location('city',null), -- IP 值为 null ，返回 null
      ip_location('city','36.98.202.245') -- 返回值为 0 ，说明未收录该 IP 该项位置信息
    ;
    ```
- **idcard_parse**(mode,idcard) -> int
    - 函数描述    
      用于解析中国居民身份证号，可解析性别、计算年龄
    - 参数说明    
        - mode，查询模式，字符串类型，支持参数：age（年龄）、sex（性别，1男，0女）    
        - idcard，15或18位身份证号，字符串类型    
    - 使用示例（示例所用数据为基于身份证号规则随机生成，仅供测试使用）
    ```sql
    select
      idcard_parse('Sex','422802199906186030') "性别", -- 1男，0女
      idcard_parse('age','422802199906186030') "年龄",
      idcard_parse('age','422802199906186031'), -- 非法身份证号码，返回 null
      idcard_parse('Age',null) -- 身份证为 null，返回 null
    ;
    ```

### 使用说明
1. 克隆项目到本地，`git clone project`    
2. 编译项目，`mvn clean package`    
3. 得到插件 zip 包：`presto-udfs-340-bundle.zip`    
4. 解压 zip 文件至 Presto SQL 安装目录 **plugin** 目录下    
5. 重启 Presto SQL    