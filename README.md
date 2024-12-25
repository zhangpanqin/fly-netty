## 项目配置

插件版本推荐在 **`gradle.properties`** 配置 
依赖版本使用 [gradle version catalog](https://docs.gradle.org/current/userguide/platforms.html#sub:version-catalog-declaration)

所有的插件和依赖从那个地方下载，全部在 **`settings.gradle`** 配置，不推荐在单独 project 配置。

## 项目集成
### SpringBoot 和 spring cloud alibaba
spring boot 2.7
spring cloud alibaba 2021.0.5.0
spring-cloud 2021.0.5

### 静态分析
- 集成了 pmd 和 alibaba p3c
- checkstyle
- spotbugs

### IDEA 插件
- Alibaba cloud Toolkit
- Checkstyle-IDEA
- SpotBugs
