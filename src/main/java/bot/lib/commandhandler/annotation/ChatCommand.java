package bot.lib.commandhandler.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ChatCommand {
    String name();
    Class<?> argsClass() default NoArgs.class;
}
