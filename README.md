## 代码生成器

基于 JPA 定义的 Entity 自动生成 CRUD 代码，只需定义好 Entity 即可，无需编写任何 Dao、Service、Controller 代码。

```java

@Data
@Entity
@Table(name = "t_example")
public class Example {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("主键")
    private Long id;

    @Column(name = "username")
    @Comment("用户名")
    @NotBlank
    private String username;

    @Comment("性别")
    private Byte sex;

    @Comment("头像")
    private String avatar;

    @Comment("密码")
    @NotBlank
    private String password;

    @Comment("状态")
    private Byte status;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Comment("创建时间")
    private Timestamp createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Comment("修改时间")
    private Timestamp updateTime;
}
```

基于 Entity 生成代码如下，可对方法进行重载实现自己的功能：

```java
// 生成的 CRUD Repository
@Repository
public interface ExampleRepository extends BaseRepository<Example, Long> {
}
```

```java
// 生成的 CRUD Service生成的 CRUD Service
@Service
public class ExampleService extends BaseService<Example, Long, ExampleRepository> {
}
```

列表和分页高级查询

```java
// 生成的高级查询代码需要结合配置进行设置
@Data
@EqualsAndHashCode(callSuper = true)
@Query(Example.class)
@QueryLike(value = {"username"})
@QueryEqual(value = {"status"})
@QueryBetween(value = "createTime")
public class ExampleQuery extends BaseQueryAccess {
    private Byte status;
}
```

基础的 CRUD 接口：

```bash
# 新增
POST:/example
# 批量删除
DELETE:/example
# 修改
PUT:/example
# 条件+查询全部
GET:/example/list
# 分页+条件查询
GET:/example/page
# 查询单个详情
GET:/example/info/{id}
```

```java
//  生成的 CRUD Controller
@RestController
@ResponseResult
@RequestMapping("/example")
public class ExampleController extends BaseController<Example, Long, ExampleQuery, ExampleService> {
}
```

具体的配置参考 `GeneratorConfig` 类。

## Result 类型返回

只需在 Controller 类或者方法上添加 `@ResponseResult` 注解即可。

```json
{
  "c": 200,
  "m": "",
  "d": []
}
```

```java

@RestController
@ResponseResult
@RequestMapping("/example")
public class ExampleController extends BaseController<Example, Long, ExampleQuery, ExampleService> {
}
```