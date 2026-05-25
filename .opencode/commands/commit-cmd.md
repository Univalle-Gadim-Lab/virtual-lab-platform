---
description: Generate, improve, or refactor professional Git commit messages.
agent: commit-sa
---

# Instructions

- Use the optional context provided in `$ARGUMENTS` as input for commit generation.
- The `commit` SKILL MUST always be used.
- Generate commits following Conventional Commits standards.
- Use concise and professional technical English.
- Improve grammar, clarity, formatting, and commit structure.
- Detect the real intention of the changes and generate meaningful commit descriptions.
- Avoid vague wording, redundant information, and implementation noise.
- Generate production-quality commits suitable for enterprise repositories.

# Fallback Behavior

If the `commit` SKILL is unavailable:
- Stop immediately.
- Do not generate commits manually.
- Report that the task cannot continue without the required skill.