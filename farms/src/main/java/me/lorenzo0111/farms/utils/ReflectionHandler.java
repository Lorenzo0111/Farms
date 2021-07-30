package me.lorenzo0111.farms.utils;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public final class ReflectionHandler<T> {
    private final String packageName;
    private final Class<? extends T> subType;
    private final Class<?>[] paramTypes;

    @Contract("_, _, _ -> new")
    public static <T> @NotNull ReflectionHandler<T> with(String packageName, Class<? extends T> subType, Class<?>... paramTypes) {
        return new ReflectionHandler<>(packageName, subType, paramTypes);
    }

    public @NotNull List<T> build(Object... params) {
        List<T> list = new ArrayList<>();

        for (Class<? extends T> clazz : new Reflections(packageName)
                .getSubTypesOf(subType)) {
            try {
                list.add(clazz.getDeclaredConstructor(paramTypes).newInstance(params));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        return list;
    }

}
