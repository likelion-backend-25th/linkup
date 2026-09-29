package net.likelion.bebc25.linkup.search.dto;

import java.util.List;

public record CursorResponse<T>(
        List<T> content,
        Long nextCursor,
        boolean hasNext
) {

    public static <T> CursorResponse<T> of(
            List<T> content,
            Long nextCursor,
            boolean hasNext
    ) {
        return new CursorResponse<>(
                content,
                nextCursor,
                hasNext
        );
    }
}
