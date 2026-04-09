package com.cmcu.itstudy.util;

import java.text.Normalizer;
import java.util.Locale;

public class SlugUtils {

    public static String slugify(String input) {
        if (input == null || input.isBlank()) {
            return "default-slug"; // Provide a default slug or handle as an error
        }
        String s = Normalizer.normalize(input.trim(), Normalizer.Form.NFD).replaceAll("\p{M}+", "");
        s = s.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("^-+|-+$", "");
        return s.isEmpty() ? "default-slug" : s; // Ensure slug is not empty
    }

    public static String resolveSlug(String requestedSlug, String name) {
        String raw = requestedSlug != null && !requestedSlug.isBlank() ? requestedSlug.trim() : null;
        return slugify(raw != null ? raw : name);
    }
}
