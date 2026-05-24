---
description: Add or update Javadocs for Java files in a folder
agent: javadocum
---

- Read the folder specified in `$1`. If the user doesn't provide a folder path, ASK them to enter one.
- If `$2` is empty: Add or update Javadocs for all Java files in the folder (non-recursive).
- If `$2` is not empty: Add or update Javadocs for all Java files in the folder (non-recursive) that match the specified pattern.
- Use the `javadoc` skill to generate or update Javadocs.