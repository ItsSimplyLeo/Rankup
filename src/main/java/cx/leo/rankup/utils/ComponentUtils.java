package cx.leo.rankup.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

import java.util.List;
import java.util.stream.Collectors;

public class ComponentUtils {

    private final static MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component parse(String content, TagResolver... resolvers) {
        return MINI_MESSAGE.deserialize(content, resolvers);
    }

    public static List<Component> parseToList(List<String> content, TagResolver... resolvers) {
        return content.stream().map(c -> parse(c, resolvers)).collect(Collectors.toList());
    }

}
