package bot.lib.commandhandler.annotation;

import net.dv8tion.jda.api.Permission;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ChatCommand {
    String name();
    Class<?> argsClass() default NoArgs.class;
    Permission[] requiredPerms() default {};
    /* This field is supposed to be for the perms the bot requires, but that is perhaps useless */
    // Permission[] botRequirePerms() default {};

}
