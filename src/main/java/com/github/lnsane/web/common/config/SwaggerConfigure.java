package com.github.lnsane.web.common.config;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.classmate.TypeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import static com.github.lnsane.web.common.utils.SwaggerUtils.customMixin;
import static com.github.lnsane.web.common.utils.SwaggerUtils.propertyBuilder;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * @author lnsane
 */
//@Configuration
//@EnableSwagger2
public class SwaggerConfigure {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //.title("swagger-bootstrap-ui-demo RESTful APIs")
                        .description("# swagger-bootstrap-ui-demo RESTful APIs")
                        .termsOfServiceUrl("http://www.xx.com/")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("2.X版本")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.github.lnsane.web.common.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

//    @Bean
    public AlternateTypeRuleConvention customizeConvention(TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.LOWEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return Arrays.asList(
//                        newRule(resolver.resolve(Page.class, WildcardType.class),
//                        resolver.resolve(CustomizedPage.class, WildcardType.class)),
                        newRule(resolver.resolve(Page.class), resolver.resolve(pageableMixin())));
            }
        };
    }


    private Type pageableMixin() {
        return customMixin(Page.class, Arrays.asList(
                propertyBuilder(Integer.class, "current"),
                propertyBuilder(Integer.class, "size")
        ));
    }

    /**
     * Alternative page type.
     *
     * @param <T> content type
     * @author johnniang
     */
    interface CustomizedPage<T> {

        List<T> getContent();

        int getPage();

        int getPages();

        long getTotal();

        int getRpp();

        boolean getHasNext();

        boolean getHasPrevious();

        boolean getIsFirst();

        boolean getIsEmpty();

        boolean getHasContent();

    }
}
