package crain.client.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NullUtil {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(NullUtil.class);

    public static boolean anyNull(Object... objectArr) {
        return Arrays.stream(objectArr).anyMatch(Objects::isNull);
    }

    public static boolean anyNull(Collection<?> nullableCollection) {
        return nullableCollection == null || nullableCollection.stream().anyMatch(Objects::isNull);
    }

    public static boolean anyNullField(Object obj) {
        return obj == null || anyNullFieldHelper(obj);
    }

    public static boolean allNullFields(Object obj) {
        return Arrays.stream(obj.getClass().getDeclaredFields()).allMatch(field -> {
            try {
                return field.get(obj) == null;
            } catch (Exception e) {
                log.error("Failed to Check a field {}", e.getMessage(), e);
                return true;
            }
        });
    }

    public static <T> T getPotentialNull(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            log.error("getPotentialNull Failed {}", e.getMessage(), e);
            return null;
        }
    }

    private static boolean anyNullFieldHelper(Object nonNullObject) {
        return Arrays.stream(nonNullObject.getClass().getDeclaredFields()).anyMatch(field -> {
            try {
                field.setAccessible(true);
                return field.get(nonNullObject) == null;
            } catch (Exception e) {
                log.error("Failed to Check a field {}", e.getMessage(), e);
                return true;
            }
        });
    }
}
