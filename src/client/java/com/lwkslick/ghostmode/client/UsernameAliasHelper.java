package com.lwkslick.ghostmode.client;

public class UsernameAliasHelper {

    private static String normalise(String input) {
        return input.toLowerCase()
                .replace(" ", "")
                .replace(".", "")
                .replace("_", "")
                .replace("-", "")
                .replace("+", "")
                .replace("*", "")
                .replace(",", "")
                .replace("1", "l")
                .replace("!", "l")
                .replace("|", "l")
                .replace("7", "t")
                .replace("3", "e")
                .replace("4", "a")
                .replace("@", "a")
                .replace("0", "o")
                .replace("5", "s")
                .replace("$", "s")
                .replace("6", "g")
                .replace("9", "g")
                .replace("2", "z")
                .replace("8", "b")
                .replace("v", "u")
                .replace("а", "a")
                .replace("е", "e")
                .replace("о", "o")
                .replace("р", "p")
                .replace("с", "s")
                .replace("х", "x")
                .replace("у", "y")
                .replace("і", "i")
                .replace("к", "k")
                .replace("ӏ", "l")
                .replaceAll("(.)\\1+", "$1");
    }

    public static String replace(String text, String name, String alias, boolean fuzzy) {
        if (text == null || text.isEmpty()) return text;

        // Always do case-insensitive exact match
        String result = text.replaceAll("(?i)" + java.util.regex.Pattern.quote(name), alias);

        if (!fuzzy) return result;

        String normName = normalise(name);

        // Build a parallel normalised index:
        // normIndex[i] = position in original text that produced normChar[i]
        // This lets us find where a normalised match starts/ends in the original string
        int[] normToOrig = new int[result.length()]; // over-allocate, we'll track actual size
        StringBuilder normBuilder = new StringBuilder();

        for (int i = 0; i < result.length(); i++) {
            // normalise single char to see if it contributes
            String nc = normalise(String.valueOf(result.charAt(i)));
            if (!nc.isEmpty()) {
                // each char in nc maps back to position i
                for (int j = 0; j < nc.length(); j++) {
                    if (normBuilder.length() < normToOrig.length) {
                        normToOrig[normBuilder.length()] = i;
                    }
                    normBuilder.append(nc.charAt(j));
                }
            }
            // if nc is empty (stripped char like space), it doesn't appear in norm — skip
        }

        String normText = normBuilder.toString();
        int normLen = normName.length();

        // Find all matches of normName in normText, map back to original positions
        StringBuilder out = new StringBuilder(result);
        int offset = 0; // tracks how much the string has grown/shrunk from replacements
        int searchFrom = 0;

        while (true) {
            int matchStart = normText.indexOf(normName, searchFrom);
            if (matchStart == -1) break;
            int matchEnd = matchStart + normLen - 1;

            // Map back to original string positions
            int origStart = normToOrig[matchStart];
            int origEnd = normToOrig[matchEnd];

            // origEnd is the START of the last contributing char — advance past it
            int origEndExclusive = origEnd + 1;
            // Also consume any trailing stripped chars (spaces etc.) that belong to this match
            // by advancing while the next char normalises to empty
            while (origEndExclusive < result.length() &&
                    normalise(String.valueOf(result.charAt(origEndExclusive))).isEmpty()) {
                // only consume if it's sandwiched — don't eat trailing spaces after the name
                break;
            }

            out.replace(origStart + offset, origEndExclusive + offset, alias);
            offset += alias.length() - (origEndExclusive - origStart);

            searchFrom = matchEnd + 1;
        }

        return out.toString();
    }
}