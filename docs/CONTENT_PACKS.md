# ModernMayhem Content Packs

Add your own gear to ModernMayhem — armor, backpacks, chest rigs, cosmetic curios, night vision and
thermal goggles — using **GeckoLib Bedrock models and JSON only. No Java, no compiling.**

---

## 1. Where packs go

Drop your pack directly into:

```
.minecraft/modernmayhem/
```

Each pack is a **direct child** of that folder and may be either a **folder** or a **`.zip`** —
you do not need to unzip anything.

```
.minecraft/modernmayhem/
  MyGearPack/            <- folder pack
  AnotherPack.zip        <- zip pack, works as-is
```

> **Restart required.** Minecraft locks its item list during startup, so ModernMayhem reads packs
> while the game is loading. **Adding, removing or renaming an item needs a game restart.**
> (Textures, models and the `display` framing *do* reload live with **F3+T** — see §7.)

> **Multiplayer:** a pack must be installed on **both the server and every client**, exactly like a
> normal content mod. Mismatched item lists will disconnect players.

### 1.1 The default pack — ModernMayhem's own gear

You'll find a pack already there:

```
.minecraft/modernmayhem/
  ModernMayhem/          <- written by the mod on every launch
```

**ModernMayhem's own armor, backpacks, rigs, cosmetics and goggles are defined by that pack**, using
exactly the format described here. It is the reference implementation — if you want to know how
something is done, read it.

It is rewritten from the mod jar every time the game starts, so edits to it are lost. To keep your
changes, create `.minecraft/modernmayhem/modernmayhem.properties` (the mod writes one for you with
comments) and set:

```properties
regenerate_default_pack=false
```

ModernMayhem then leaves the folder alone, and your edits — including deleting items you don't
want — stick.

> **The trade-off:** with regeneration off you also stop receiving changes to MM's own gear when the
> mod updates: new items, rebalances and model fixes. Turn it off deliberately, and expect to
> reconcile by hand after an update.

> Retexturing MM's gear is better done with an ordinary **resource pack** — it survives mod updates
> and doesn't require freezing the default pack.

---

## 2. Pack layout

A pack is laid out exactly like a combined **resource pack + datapack**, so if you have made either
before this will look familiar:

```
MyGearPack/
  pack.mcmeta                                  <- standard Minecraft pack metadata
  data/
    mygearpack/                                <- your namespace
      modernmayhem/
        armor/
          desert_chest.json                    <- armor definitions
          desert_helmet.json
        curio/
          desert_backpack.json                 <- backpack / rig / cosmetic definitions
        goggles/
          desert_nvg.json                      <- night vision / thermal / visor definitions
        keybind/
          open_belt.json                       <- optional "open this slot" keys
  assets/
    mygearpack/                                <- same namespace
      geo/armor/desert_chest.geo.json          <- GeckoLib model
      textures/armor/desert_chest.png          <- model texture
      animations/empty.animation.json
      lang/en_us.json                          <- display names
      sounds.json                              <- optional, your own sounds
      sounds/desert_nvg_on.ogg
```

Both halves are loaded for you: `assets/` is mounted as a resource pack and `data/` as a datapack, so
**tags, recipes, loot tables and advancements in your pack all work normally** — no extra setup.

### 2.1 Your own sounds

Drop `.ogg` files under `assets/<namespace>/sounds/` and describe them in
`assets/<namespace>/sounds.json`, exactly as a resource pack does:

```json
{
  "desert_nvg_on": {
    "subtitle": "subtitles.mygearpack.desert_nvg_on",
    "sounds": ["mygearpack:desert_nvg_on"]
  }
}
```

ModernMayhem registers a sound event for every entry, so you can then reference
`mygearpack:desert_nvg_on` anywhere a sound id is accepted:

```json
"sounds": { "activate": "mygearpack:desert_nvg_on", "deactivate": "mygearpack:desert_nvg_off" }
```

> A resource pack can ship the audio but cannot create the registry entry — that is the part this
> does for you. As with items, **adding or removing sounds needs a game restart.**

`pack.mcmeta` is the ordinary Minecraft one:

```json
{
  "pack": {
    "pack_format": 15,
    "description": "My Gear Pack"
  }
}
```

### Item IDs come from the file path

You do **not** write an `id` field. The item's ID is derived from where the file sits:

```
data/mygearpack/modernmayhem/armor/desert_chest.json   ->   mygearpack:desert_chest
```

Keep your `assets/<namespace>/` folder on the **same namespace** as your `data/<namespace>/` folder
and your textures will always resolve.

---

## 3. Armor definition

`data/<namespace>/modernmayhem/armor/<name>.json`

```json
{
  "type": "armor",
  "slot": "chest",
  "material": "kevlar",

  "model": "mygearpack:geo/armor/desert_chest.geo.json",
  "texture": "mygearpack:textures/armor/desert_chest.png",
  "animation": "mygearpack:animations/empty.animation.json",

  "slim_model": "mygearpack:geo/armor/desert_chest_thin.geo.json",
  "slim_texture": "mygearpack:textures/armor/desert_chest_thin.png",

  "stats": {
    "protection": 8,
    "toughness": 3,
    "knockback": 0.1,
    "durability": 400
  }
}
```

### Fields

| Field | Required | Description |
|---|---|---|
| `type` | yes | Always `"armor"` for this folder. |
| `slot` | yes | `helmet`/`head`, `chestplate`/`chest`, `leggings`/`legs`, `boots`/`feet`. |
| `model` | yes | GeckoLib `.geo.json` resource location. |
| `texture` | yes | Texture resource location. |
| `material` | no | Free-form grouping label. Default `generic`. |
| `animation` | no | Defaults to ModernMayhem's empty animation. |
| `slim_model` / `slim_texture` | no | Used automatically for players on the slim ("Alex") skin model. **If you omit them, the normal `model`/`texture` is used for slim players too** — so you only need to provide these if your model actually needs a slim variant. The two fall back independently (you can supply a slim texture without a slim model, or vice versa). |
| `stats` | no | See below. |
| `display` | no | Per-context framing — see §6. |
| `features` | no | Capability flags — see §3.1. |
| `screen_overlay` | no | Full-screen texture drawn while this helmet is worn in first person (a visor HUD, scratched glass, etc.). ModernMayhem's Ronin helmet uses this. Helmets only. |
| `hide_skin_overlay` | no | Hides the player skin's outer layer under this piece — see below. Defaults to `false`. |
| `tooltip` | no | Extra description lines — see §9. |
| `effects` | no | Mob effects granted while worn — see below. |

### `effects` — mob effects while worn

```json
"effects": [
  { "effect": "minecraft:water_breathing" },
  { "effect": "minecraft:invisibility", "requires_full_set": true, "show_particles": false }
]
```

| Key | Default | Description |
|---|---|---|
| `effect` | — | Effect id. |
| `amplifier` | `0` | `0` is level I, `1` is level II, ... |
| `requires_full_set` | `false` | Only apply while all four armor slots hold pieces of the same `material`. |
| `show_particles` | `true` | Whether the ambient particles are shown. |
| `show_icon` | `true` | Whether the effect appears in the inventory HUD. |

The effect is refreshed while the piece is worn and lapses a moment after it comes off.

**Set bonuses** use `requires_full_set`. The set is identified by the `material` field, so a ghillie
suit is four pieces that all declare `"material": "ghillie"`, with the effect on whichever piece you
like — put it on one, since listing it on all four just applies the same effect repeatedly.

> Vanilla `minecraft:invisibility` already hides the player while leaving their **armor** visible, so
> it gives the "only the suit shows" look with no extra work.

An effect belonging to a mod that isn't installed is skipped silently, so it's safe to reference one
from a pack that doesn't hard-depend on it.

### `hide_skin_overlay` — building armor at the body's true size

Minecraft skins have a second, outer layer (the "jacket" and "sleeves" in the skin editor). Vanilla
armor doesn't hide it — vanilla armor models are drawn slightly **inflated** so they clear it.

That means a model built to sit flush against the body occupies the same space as the skin overlay,
and the two z-fight. The usual workaround is to inflate your model until it clears — which changes
the silhouette you actually wanted. Instead:

```json
"hide_skin_overlay": true
```

The overlay is then hidden underneath that piece while it's worn, so the model can be built at the
body's true dimensions.

Which parts get hidden follows the slot:

| Slot | Hides |
|---|---|
| helmet | hat |
| chestplate | jacket, both sleeves |
| leggings | both trouser layers |
| boots | both trouser layers |

> Legs and feet share one pair of overlay parts — a skin has no separate boot overlay — so setting
> this on **boots** hides the whole leg overlay, thigh included. If your boots only cover the ankle,
> leave it off for them and set it on the leggings instead.

It's per piece, so a set can mix: a flush chestplate that hides the jacket alongside a helmet that
leaves the hat layer alone.

### 3.1 `features` — capabilities

```json
"features": {
  "head_mount": true,
  "visor_mount": false
}
```

| Flag | Meaning |
|---|---|
| `head_mount` | This helmet can carry NVGs / thermal goggles. |
| `visor_mount` | This helmet can carry a visor. |

Unknown keys are logged as a warning so typos don't fail silently.

### 3.1.1 The same thing with tags (and how to support other mods' gear)

`features` is a **convenience for your own items**. The underlying mechanism is ModernMayhem's item
tags, which remain fully supported — a capability applies if **either** the tag says so **or** the
`features` block does.

Tags are the only way to affect items you didn't create, so use them to make **another mod's or
vanilla's** helmet NVG-compatible. Since your pack's `data/` folder is loaded as a datapack, just drop
this in:

`data/mm/tags/items/has_head_mount.json`

```json
{
  "replace": false,
  "values": [
    "minecraft:iron_helmet",
    "someothermod:tactical_helmet"
  ]
}
```

Keep `"replace": false` so you add to the tag instead of wiping everyone else's entries.

Available tags: `mm:has_head_mount`, `mm:has_visor_mount`, `mm:gas_mask_incompatible`.

### `stats`

| Key | Default | Description |
|---|---|---|
| `protection` | `0` | Armor points. |
| `toughness` | `0` | Armor toughness. |
| `knockback` | `0` | Knockback resistance (`0.0`–`1.0`). |
| `durability` | `400` | Max durability for this piece. |

Values in the JSON are authoritative.

---

## 4. Model requirements (important)

Your armor `.geo.json` must use the **standard GeckoLib armor bone names**:

```
armorHead
armorBody
armorRightArm    armorLeftArm
armorRightLeg    armorLeftLeg
armorRightBoot   armorLeftBoot
```

These are what GeckoLib maps onto the player's body. **You only need one model containing the whole
humanoid** — ModernMayhem automatically shows just the bones belonging to the item's slot:

| Slot | Bones shown |
|---|---|
| `helmet` | `armorHead` |
| `chestplate` | `armorBody`, `armorRightArm`, `armorLeftArm` |
| `leggings` | `armorRightLeg`, `armorLeftLeg` |
| `boots` | `armorRightBoot`, `armorLeftBoot` |

This applies both when worn **and** in the inventory icon, so you do **not** need to build a second,
trimmed-down model just for the icon.

### One model per set, or one per piece — both work

Because the slot filter picks out the bones it needs, you can either:

- **Share one model across a whole set** — author a single `.geo.json` (and single texture) containing
  head, body, arms, legs and boots, then point all four definitions at it. Each piece automatically
  shows only its own bones.
- **Use a separate model per piece** — handy when pieces are mix-and-match (e.g. several different
  helmets that all pair with the same body armor), or when they use different texture sheets.

Pick whichever suits your set. Note that sharing one model requires the pieces to share **one texture
sheet**, since a definition points at a single texture.

---

## 5. Curios — backpacks, chest rigs and cosmetics

`data/<namespace>/modernmayhem/curio/<name>.json`

Backpacks, rigs and purely cosmetic curios all use the **same** definition shape. What an item *is* is
decided by which `slot` it goes in and whether you give it `storage` and `stats`.

A backpack (storage, no armor):

```json
{
  "type": "curio",
  "slot": "back",
  "model": "mygearpack:geo/curio/big_ruck.geo.json",
  "texture": "mygearpack:textures/curio/big_ruck.png",
  "storage": {
    "rows": 3,
    "columns": 9
  }
}
```

A chest rig (storage **and** armor, and it can feed ammo to guns):

```json
{
  "type": "curio",
  "slot": "body",
  "model": "mygearpack:geo/curio/scout_rig.geo.json",
  "texture": "mygearpack:textures/curio/scout_rig.png",
  "storage": {
    "rows": 1,
    "columns": 4,
    "supplies_ammo": true
  },
  "stats": {
    "protection": 7,
    "toughness": 2,
    "knockback": 0.3,
    "durability": 400
  }
}
```

A cosmetic curio is just one with **no `storage` and no `stats`** — for example a balaclava in the
`head` slot.

### Fields

| Field | Required | Description |
|---|---|---|
| `type` | yes | Always `"curio"` for this folder. |
| `slot` | yes | `back`, `body`, `head`, `knees`, `earwear` or `facewear`. |
| `model` | yes | GeckoLib `.geo.json` resource location. |
| `texture` | yes | Texture resource location. |
| `animation` | no | Defaults to ModernMayhem's empty animation. |
| `attaches_to` | no | Which body part the model rides on — see below. Defaults from `slot`. |
| `storage` | no | Inventory config — see below. Omit for a cosmetic item. |
| `stats` | no | Same keys as armor (§3). Gives the curio armor value + durability. |
| `attributes` | no | Any other attribute modifiers to grant while worn — see below. |
| `conflicts_with_helmets` | no | Item tag of helmets this curio refuses to be worn with — see below. |
| `immune_to_effects` | no | Mob-effect tag the wearer is immune to — see below. |
| `transparent` | no | `true` for see-through gear. Defaults to `false` — see below. |
| `tooltip` | no | Extra description lines — see §9. |
| `display` | no | Per-context framing — see §6. |

### `storage`

| Key | Default | Description |
|---|---|---|
| `rows` | `0` | Inventory rows, `0`–`9`. |
| `columns` | `0` | Inventory columns, `0`–`9`. |
| `supplies_ammo` | `false` | Whether guns may draw ammo straight from this curio. |

A curio with no storage (or `0` in either dimension) simply doesn't open an inventory.

### `attributes` — anything beyond armor

`stats` covers the armor-ish numbers, because those scale down as the item takes damage. For anything
else — movement speed, attack damage, an attribute from another mod — use `attributes`:

```json
"attributes": [
  { "attribute": "mm:safe_fall_distance", "amount": 4 },
  { "attribute": "minecraft:generic.movement_speed", "amount": -0.05, "operation": "multiply_total" }
]
```

| Key | Default | Description |
|---|---|---|
| `attribute` | — | Attribute id. |
| `amount` | — | How much to add or multiply by. |
| `operation` | `addition` | `addition`, `multiply_base` or `multiply_total`. |
| `name` | attribute path | Label for the modifier, shown in the tooltip. |

These are applied flat — unlike `stats` they are **not** scaled by remaining durability, since an item
granting them needn't be damageable at all. ModernMayhem's kneepads use this for their extra safe fall
distance.

> If the attribute belongs to a mod that isn't installed, it's skipped silently rather than breaking
> the item — so it's safe to grant another mod's attribute in an optional-dependency pack.

### `conflicts_with_helmets` — refusing to share the head

Some headgear can't physically coexist with a helmet. Point this at an **item tag** of the helmets to
refuse:

```json
"conflicts_with_helmets": "mm:gas_mask_incompatible"
```

The curio then can't be equipped while such a helmet is worn, and if the player puts the helmet on
afterwards the curio is moved back into their inventory (dropped if there's no room). ModernMayhem's
GP-5 gas mask uses exactly this — and because it's a tag, you can list *other mods'* helmets in it too.

### `immune_to_effects` — filtering out mob effects

A gas mask should keep gas out. Point this at a **tag of mob effects** the wearer becomes immune to
while the curio is equipped:

```json
"immune_to_effects": "mm:gas_mask_immunity"
```

The tag is an ordinary datapack file, at `data/<namespace>/tags/mob_effect/<name>.json` — note
`mob_effect`, singular, unlike `tags/items`:

```json
{
  "replace": false,
  "values": ["minecraft:poison", "minecraft:wither"]
}
```

Blocked effects can't be applied while the curio is worn, and any already running are cleared shortly
after putting it on.

Because it's a tag, **anyone can extend it without touching your pack.** ModernMayhem's GP-5 uses
`mm:gas_mask_immunity`; a mod adding its own toxic effect can drop a file into that tag and the mask
protects against it, with neither side knowing about the other. Equally, you can point your own mask
at your own tag to keep it separate.

### `transparent` — see-through gear

By default a texel is either fully solid or fully invisible: any partial alpha in between is rounded
away. That's what you want for fabric, webbing and plastic.

For tinted glass — goggle lenses, shooting glasses, a visor — set:

```json
"transparent": true
```

Partial alpha in the texture is then blended properly, both **on the player** and **in the inventory
icon**. ModernMayhem's glasses and goggles use this.

Only switch it on when the texture really has partial alpha, because it isn't free:

- Transparent surfaces sort against each other by **draw order**, not depth, so two of them layered
  in the same model can look wrong from some angles. Opaque gear never has this problem.
- It breaks render batching for that item.
- If **Accelerated Rendering** is installed, the wearer drops back to the vanilla entity pipeline for
  as long as they're wearing the item, because the accelerated one draws gear before the body it sits
  on and the wearer's head would disappear behind the lens.

None of that matters for the handful of items that genuinely need it, and all of it matters if every
item in your pack sets the flag.

### You do **not** need a Curios tag

Curios normally decides slot eligibility from `curios:<slot>` item tags. Since your definition already
declares its `slot`, ModernMayhem generates that tag entry for you — **just write the one JSON file.**

(Those generated tags are added with `"replace": false`, so they merge cleanly with ModernMayhem's own
items and anything other mods or packs contribute.)

### Inventing your own slot

You are not limited to ModernMayhem's slots. Curios slot *types* are themselves datapack-defined, and
your pack's `data/` folder is a datapack — so you can create a brand new slot (a belt, a shoulder
pouch, whatever) with two extra files:

`data/mygearpack/curios/slots/belt.json`

```json
{
  "size": 1,
  "icon": "mygearpack:slot/belt_icon"
}
```

`data/mygearpack/curios/entities/mygearpack.json`

```json
{
  "entities": ["player"],
  "slots": ["belt"]
}
```

Then just use it in your definition:

```json
{ "type": "curio", "slot": "belt", "...": "..." }
```

ModernMayhem accepts any slot name and generates the matching `curios:belt` item tag for you. If you
use a slot it doesn't ship, it logs a reminder to include the two files above.

Custom slots that hold **storage** can be opened by right-clicking the item — or give the slot its own
dedicated key, below.

### Giving your slot an "open" key

`data/<namespace>/modernmayhem/keybind/<name>.json`

ModernMayhem's built-in "open backpack" / "open rig" keys are tied to the `back` and `body` slots. If
you invent your own slot, you can give it a key too:

```json
{
  "type": "keybind",
  "opens_slot": "belt",
  "default_key": "k"
}
```

| Field | Required | Description |
|---|---|---|
| `type` | yes | Always `"keybind"`. |
| `opens_slot` | yes | The Curios slot whose curio this key opens. |
| `default_key` | no | Default binding, e.g. `"k"` or the full `"key.keyboard.k"`. Unbound if omitted. |

The key appears in the vanilla **Options → Controls** screen under "Modern Mayhem" and players can
rebind it like any other. Name it with a lang entry (§9), using the key `key.<namespace>.<name>`:

```json
{ "key.mygearpack.open_belt": "Open Belt" }
```

> **Multiplayer:** on joining, the server tells the client which Curios slots it actually has. If a
> player has your pack but the server doesn't, the key simply does nothing instead of sending the
> server requests it can't serve. Nothing to configure — it just works.

### Curio models — attaching to the player's body

Curio models use the **same GeckoLib armor bone names as armor** (§4), and this is what makes them
move with the player. Each bone is mapped onto the matching part of the player's skeleton, so whatever
you put inside `armorBody` follows the torso's rotation, `armorHead` follows head movement, and so on.

| Put your geometry in | It follows |
|---|---|
| `armorBody` | the torso |
| `armorRightArm` / `armorLeftArm` | the arms |
| `armorHead` | the head |
| `armorRightLeg` / `armorLeftLeg` | the legs |
| `armorRightBoot` / `armorLeftBoot` | the feet |

A belt, backpack, chest rig or bandoleer all belong in **`armorBody`** — they ride on the torso and
turn as the player turns. (ModernMayhem's own backpack and plate carrier models do exactly this.)

### `attaches_to`

Which group of bones gets drawn is controlled by `attaches_to`:

```json
{ "type": "curio", "slot": "belt", "attaches_to": "chest", "...": "..." }
```

| Value | Bones drawn |
|---|---|
| `chest` (aka `body`, `torso`) | `armorBody` + both arms |
| `head` | `armorHead` |
| `legs` | both legs |
| `feet` | both boots |

You usually don't need to set it — the default is derived from your Curios slot:

| Curios slot | Default `attaches_to` |
|---|---|
| `back`, `body`, **and any custom slot** | `chest` |
| `head`, `facewear`, `earwear` | `head` |
| `knees` | `legs` |

Set it explicitly when a slot's natural body part isn't where the model lives — for example a thigh
holster in a custom `holster` slot would want `"attaches_to": "legs"`.

> **If your curio is invisible when worn**, this is almost always the cause: the geometry is in a bone
> group that `attaches_to` isn't drawing (e.g. leg bones while attached to `chest`).

---

## 5.1 Goggles — night vision, thermal and visors

`data/<namespace>/modernmayhem/goggles/<name>.json`

```json
{
  "type": "goggles",
  "goggle_type": "night_vision",

  "worn":         { "model": "...", "texture": "...", "animation": "...",
                    "coti": { "model": "...", "texture": "...", "animation": "..." } },
  "first_person": { "model": "...", "texture": "...", "animation": "..." },

  "sounds":   { "activate": "mm:sound_nvg_on", "deactivate": "mm:sound_nvg_off" },
  "features": { "can_hold_coti": true, "auto_gain": true, "auto_gating": true },

  "default_gain": 1,
  "gain_steps": [
    { "brightness": 0.45, "color": [0.2, 0.5, 0.2],
      "overlay": "mm:textures/screens/pvs7_overlay.png",
      "noise": 1.2, "auto_gain_speed": 0.06, "auto_gain_offset": 0.3,
      "auto_gating_offset": 0.2, "auto_gating_speed": 0.04 }
  ]
}
```

| Field | Required | Description |
|---|---|---|
| `goggle_type` | no | `night_vision` (default), `thermal` or `visor`. |
| `worn` | yes | Model/texture/animation seen on the player, plus an optional `coti` variant of each. |
| `first_person` | no | Same shape, drawn in front of the camera. Falls back to `worn`. |
| `sounds` | no | `activate` / `deactivate` — any sound id, including your own (see §2.1). |
| `features` | no | `can_hold_coti`, `auto_gain`, `auto_gating`, `rainbow_phosphor`. |
| `gain_steps` | no | The gain ladder — see below. |
| `default_gain` | no | Index into `gain_steps` to start on. |
| `stats` | no | Armor granted **while the goggle is down over the face** (same keys as §3). Mainly for visors. |
| `palettes` | no | Thermal palettes — see §5.2. |
| `post_chain` | no | Your own shader chain — see §5.3. |
| `tooltip` | no | Extra description lines — see §9. |

### COTI — clip-on thermal imagers

`can_hold_coti` lets a goggle accept a clip-on thermal imager. What counts as one is the item tag
**`mm:coti`**, not a fixed item, so a pack can add its own:

```json
// data/mygearpack/tags/items/... no -- put it in ModernMayhem's tag:
// data/mm/tags/items/coti.json
{ "replace": false, "values": ["mygearpack:thermal_clip"] }
```

The `worn.coti` and `first_person.coti` blocks then supply the appearance while one is attached.

### Visors

A visor is just `"goggle_type": "visor"`. It has no tubes, so it needs **no `gain_steps`** and draws no
screen shader — it's a physical faceplate that flips down. Give it `stats` to make it protect you, and
point its `first_person` texture at a transparent variant so you can see through it:

```json
{
  "type": "goggles",
  "goggle_type": "visor",
  "worn":         { "model": "...", "texture": "mypack:textures/.../visor.png", "animation": "..." },
  "first_person": { "model": "...", "texture": "mypack:textures/.../visor_transparent.png", "animation": "..." },
  "sounds": { "activate": "mm:sound_visor_close", "deactivate": "mm:sound_visor_open" },
  "stats":  { "protection": 2, "toughness": 1, "knockback": 0.1 }
}
```

> Note the asymmetry is deliberate: a visor is **solid on the player model** in third person, and
> see-through only from behind it in first person, where you need to look through it. That is why the
> transparency comes from a separate `first_person` texture and **not** from the curio `transparent`
> flag (§5) — that flag makes gear see-through on the player, which is what glasses and goggles want,
> not a visor.

> Don't rely on the first-person model as the *only* sign a visor is down: players can switch it off
> (`hideFirstPersonGoggles` in the client config). ModernMayhem shows a short "down / up" message
> above the hotbar to cover that, so your visor needs nothing extra — but a visor whose only feedback
> is a subtle model change will read as broken for those players.

Visors mount on helmets tagged `visor_mount` rather than `head_mount` (§3.1).

Goggles are always **facewear**, and (like all curios) their tag is generated for you. They still need a
helmet with `head_mount` (or `visor_mount` for visors) to be equipped — see §3.1.

### `gain_steps` — the gain ladder

Each entry is one rung the Increase/Decrease Gain keys step through.

| Key | Default | Description |
|---|---|---|
| `brightness` | `0.5` | Tube brightness at this step. |
| `color` | `[1,1,1]` | Phosphor colour — this is what makes a tube green or white. |
| `overlay` | required | Screen overlay texture (the tube shape you look through). |
| `noise` | `1.0` | Grain multiplier. |
| `auto_gain_speed` / `auto_gain_offset` | `0.05` / `0` | Auto-gain response. |
| `auto_gating_offset` / `auto_gating_speed` | `0.1` / `0.1` | Auto-gating response. |

You don't set a min/max gain — ModernMayhem derives the range from the spread of `brightness` across
your ladder.

## 5.2 Thermal palettes

A thermal goggle can define its own palettes; cycling then steps through **only yours**, never
ModernMayhem's. Omit the block to use the built-in eight.

```json
"palettes": [
  {
    "name": "Cyan Hot",
    "world_color": [0.25, 0.35, 0.55],
    "base_color": [0.0, 0.02, 0.06],
    "signature": [
      { "at": 0.35, "color": [0.0, 0.35, 0.55] },
      { "at": 0.70, "color": [0.1, 0.85, 1.0] },
      { "at": 1.00, "color": [1.0, 1.0, 1.0] }
    ]
  },
  { "name": "Magenta Hot", "world_color": [0.45, 0.30, 0.50], "signature_color": [1.0, 0.1, 0.7] }
]
```

| Key | Description |
|---|---|
| `name` | Shown when cycling. |
| `world_color` | Tint applied to the world behind the thermal image. |
| `inverted` | Inverts the world, as "Black Hot" does. |
| `base_color` | Colour at zero heat. |
| `signature` | Ordered `{ at, color }` stops — the heat gradient. |
| `signature_color` | Shorthand for a simple `base → colour → white` ramp. |
| `linear` | Blend without easing (a straight ramp). |

Use `signature_color` for a simple one-colour palette, and `signature` stops when you want a real
gradient (the built-in "Ironbow" uses five).

## 5.3 `post_chain` — using your own shader

> ⚠️ **Only do this if you are comfortable writing GLSL.** If you just want different colours, use
> `palettes` (§5.2) or `gain_steps` (§5.1) — those need no shader work at all.

```json
"post_chain": "mygearpack:shaders/post/my_nvg.json"
```

Your chain and its programs live in `assets/<namespace>/shaders/`, exactly like a vanilla post chain.

**Some passes have to be there.** ModernMayhem drives its features by setting uniforms on your chain's
passes. If no pass accepts a given uniform, that feature silently stops working — your chain still
loads and renders, it just quietly does less. On load, the log tells you exactly what is missing:

```
[MM] Custom post chain 'mygearpack:shaders/post/my_nvg.json' has no pass accepting
[OutlineColor, ThermalPalette] -- those effects will not work.
```

Uniforms driven for **night vision**: `NightVisionEnabled`, `Brightness`, `RedValue`, `GreenValue`,
`BlueValue`, `NoiseMultiplier`, `MinGain`, `MaxGain`, `AutoGainEnabled`, `AutoGainSpeed`,
`AutoGatingEnabled`, `AutoGatingOffset`, `AutoGatingSpeed`, `RenderMode`, `UseSourceColor`,
`OutlineColor`, `ThermalPalette`, `PaletteCount`, `DetailStrength`, `HandCull`.

For **thermal**: `RenderMode`, `UseSourceColor`, `OutlineColor`, `ThermalPalette`, `PaletteCount`,
`DetailStrength`, `HandCull`, `TintColor`, `InvertWorld`, `Phosphor`.

The safest starting point is to copy ModernMayhem's own chain (`mm:shaders/post/night-vision.json` or
`thermal-vision.json`) and modify it, so the required passes are already present. In particular, COTI
and the thermal overlay rely on the `mm:thermal_composite` pass — drop it and they stop working.

---

## 6. `display` — optional per-item framing

Because the icon reuses your full-body model and shows only part of it, the visible piece sits where
it would on the body rather than centred in the slot. ModernMayhem ships sensible defaults per armor
slot, so **most packs never need this block.**

If you want to fine-tune how *your* model is framed, add an optional `display` block. It mirrors the
vanilla model `display` block, so it should feel familiar:

```json
"display": {
  "gui":                   { "rotation": [30, -30, 0], "translation": [0, -14.5, 0], "scale": 0.9 },
  "fixed":                 { "rotation": [0, 0, 0],    "translation": [0, -14.5, 0], "scale": 0.9 },
  "ground":                { "rotation": [0, 0, 0],    "translation": [0, -14.5, 0], "scale": 0.5 },
  "thirdperson_righthand": { "rotation": [90, -90, 0], "translation": [0, -2, -2.75], "scale": 0.5 },
  "thirdperson_lefthand":  { "rotation": [90, -90, 0], "translation": [0, -2, -2.75], "scale": 0.5 },
  "firstperson_righthand": { "rotation": [0, 180, 0],  "translation": [0, 0, 0],      "scale": 1.0 },
  "firstperson_lefthand":  { "rotation": [0, 180, 0],  "translation": [0, 0, 0],      "scale": 1.0 },
  "head":                  { "rotation": [0, 0, 0],    "translation": [0, 0, 0],      "scale": 1.0 }
}
```

### Contexts

| Key | Where it applies |
|---|---|
| `gui` | Inventory / creative menu icon |
| `fixed` | Item frames |
| `ground` | Dropped item on the floor |
| `thirdperson_righthand` / `thirdperson_lefthand` | Held, seen in third person |
| `firstperson_righthand` / `firstperson_lefthand` | Held, seen in first person |
| `head` | Worn on the head (e.g. via other mods) |

`thirdperson` and `firstperson` are accepted as shorthands that apply to **both** hands.

### Values

- **`rotation`** — `[x, y, z]` in degrees.
- **`translation`** — `[x, y, z]` in **model pixels** (`1` px = `1/16` block). `offset` is accepted as
  an alias. Positive `y` moves the item up.
- **`scale`** — a single number (uniform) **or** `[x, y, z]`.

Every key, every context and the whole block are optional; anything you leave out falls back to the
built-in default for that armor slot.

---

## 7. Live tuning (no restart)

The `display` block is re-read every time Minecraft reloads resources. To dial your framing in:

1. Launch the game and look at the item.
2. Edit the `display` values in your JSON.
3. Press **F3 + T** in-game.
4. The framing updates instantly. Repeat until it looks right.

This works for folder packs. For a `.zip` you'd have to re-zip each time, so tune as a folder and zip
it up at the end.

> Adding or removing *items* still needs a restart — only framing, models and textures reload live.

---

## 8. Using a flat 2D icon instead of the 3D model

By default the inventory icon is your 3D GeckoLib model. If you would rather use a **plain 2D sprite**
(like a vanilla item), just ship a normal item model in your assets and ModernMayhem will leave it
alone:

`assets/mygearpack/models/item/desert_chest.json`

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "mygearpack:item/desert_chest"
  }
}
```

`assets/mygearpack/textures/item/desert_chest.png` — your 16×16 (or larger) sprite.

The rule: **if a pack supplies its own `models/item/<name>.json`, that model wins.** This also lets
advanced authors hand-write a `builtin/entity` model with their own `display` transforms if they want
total control. When no item model is present, ModernMayhem generates one pointing at the 3D renderer.

---

## 9. Names and translations

Preferred — `assets/mygearpack/lang/en_us.json`:

```json
{
  "item.mygearpack.desert_chest": "Desert Plate Carrier"
}
```

The translation key is `item.<namespace>.<name>`. This is how you name every item your pack adds -- there is no name field in the definition JSON.

### Description lines

Any armor, curio or goggle definition can carry a `tooltip` array, shown under the item's name:

```json
"tooltip": [
  "description.mygearpack.desert_chest",
  { "key": "description.mygearpack.prototype", "color": "red" }
]
```

A bare string is a translation key drawn in grey; the object form lets you pick a colour (any vanilla
formatting name — `red`, `gold`, `dark_gray`, ...). As with names, the text lives in your lang file
rather than the definition, so it can be translated.

ModernMayhem's own night vision goggles use this for their "requires a helmet mount" line, and the
gamer GPNVG for its red one.

---

## 10. Troubleshooting

| Symptom | Cause |
|---|---|
| Item doesn't exist at all | Wrong folder depth — must be `data/<ns>/modernmayhem/armor/<name>.json` (or `.../curio/<name>.json`). Check the log for `[MM] Parsed armor` / `[MM] Parsed curio`. Did you restart? |
| Curio won't go in its slot | `slot` value isn't one Curios knows (`back`, `body`, `head`, `knees`, `earwear`, `facewear`). You should **not** need a tag file. |
| Curio has no inventory | `storage` missing, or `rows`/`columns` is `0`. |
| Curio invisible when worn | Geometry is in bones `attaches_to` doesn't draw (§5), or bones aren't named `armorBody`/`armorHead`/etc. |
| Goggles won't equip | Needs a helmet with `head_mount` (or `visor_mount` for a visor) — see §3.1. |
| Goggles equip but nothing happens when toggled | Check `gain_steps` isn't empty, and that `overlay` points at a real texture. |
| Some goggle effect stopped working after setting `post_chain` | Your chain is missing a pass — the log names the exact uniforms (§5.3). |
| Curio doesn't move with the player | Geometry isn't inside a named armor bone — put it in `armorBody` to follow the torso. |
| Custom key does nothing | The server doesn't have your pack (expected — see §5), the slot is empty, or `opens_slot` doesn't match your curio's `slot`. |
| Custom key shows as `key.mypack.open_belt` | Missing lang entry — see §9. |
| Purple/black missing texture | `texture` path wrong, or `assets/<ns>/` namespace doesn't match your `data/<ns>/` namespace. |
| Nothing renders when worn | Bone names don't match §4. |
| Icon shows the whole body | Bone names don't match §4, so the slot filter can't identify the pieces. |
| Icon off-centre or wrong size | Tune `display.gui` — see §6 and §7. |
| Pack ignored entirely | Missing `pack.mcmeta` / `data` / `assets` at the pack root. Check the log for `[MM] Mounted content-pack assets`. |
| Edits to the `ModernMayhem` pack keep reverting | It is regenerated every launch. Set `regenerate_default_pack=false` — see §1.1. |
| Your own sound is silent / "sound not found" | Missing `assets/<ns>/sounds.json` entry, or the `.ogg` isn't at the path it names. Look for `[MM] Registered data-driven sound`. Needs a restart. |
| Effect from `effects` never applies | For `requires_full_set`, all four armor slots must hold pieces with the **same `material`**. Otherwise check the effect id exists — unknown ids are skipped silently. |
| Effect applies but shouldn't (gas mask) | `immune_to_effects` names a **mob-effect** tag at `tags/mob_effect/` (singular), not `tags/mob_effects/`. |
| `transparent` gear hides the player behind it | Fixed automatically, including under Accelerated Rendering. If it persists, the model is likely a closed box — see §5. |
| Armor z-fights with the player's skin | The skin's outer layer is in the way; set `hide_skin_overlay` — see §3. |
| Tooltip line shows as `description.mypack.x` | Missing lang entry — see §9. |

Useful log lines (search for `[MM]`):

```
[MM] Parsed armor 'mygearpack:desert_chest' from pack 'MyGearPack'
[MM] Loaded 1 data-driven armor definition(s)
[MM] Registered data-driven armor 'mygearpack:desert_chest'
[MM] Registered data-driven sound 'mygearpack:desert_nvg_on'
[MM] Mounted content-pack assets: MyGearPack
[MM] Wrote default pack to '...\modernmayhem\ModernMayhem' (61 file(s))
```

A broken definition is logged and skipped — it never stops the rest of your pack from loading.
