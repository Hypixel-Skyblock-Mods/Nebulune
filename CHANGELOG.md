# 0.3.2

- Track Trevor animals independently of vanilla body visibility and render distance.
- Add enabled-by-default nametag fallback for matching Trapper armor stands and text displays.
- Prefer a loaded animal over its nearby matching nametag so each target has one box and one tracer.
- Ignore ordinary animal names, unrelated holograms, pets, wrong quest rarities, and zero-health nametags.
- Add regression coverage for label matching and animal/nametag handover without duplicate tracers.

# 0.3.1

- Support Minecraft 26.1.2, 26.2, and 26.3 with official Athen 0.3.5.
- Migrate configuration, events, commands, HUDs, rendering, message actions,
  equipment keybinds, and terminal hooks to Athen's current APIs.
- Track fishing bobbers belonging to the player and ignore whip-style weapons.
- Handle current NPC custom click actions and item-display rat entities.
- Preserve Trevor auto call, auto accept, and pelt ESP.
- Check mixin targets, shadowed members, and invocation injection points in builds.
- Remove inherited CI/CD workflows and point update checks at this fork.

Source fixes informed by Gaeritag/Nebulune were reviewed and applied in this
fork's own commits. No bundled Athen beta jar was used.
