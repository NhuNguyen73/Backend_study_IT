package com.cmcu.itstudy.util;

import java.text.Normalizer;
import java.util.Locale;

public class SlugUtils {

    public static String slugify(String input) {
        if (input == null || input.isBlank()) {
            return "default-slug";
        }

        String s = Normalizer
                .normalize(input.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", ""); // FIX ở đây

        s = s.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-+|-+$", "");

        return s.isEmpty() ? "default-slug" : s;
    }

    public static String resolveSlug(String requestedSlug, String name) {
        String raw = requestedSlug != null && !requestedSlug.isBlank()
                ? requestedSlug.trim()
                : null;

        return slugify(raw != null ? raw : name);
    }
}