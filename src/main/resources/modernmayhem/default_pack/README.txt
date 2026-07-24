ModernMayhem — default content pack
===================================

This folder is ModernMayhem's own gear, shipped as an ordinary content pack. Everything in here uses
exactly the same format your own packs do, so it doubles as a complete worked example: find something
that behaves the way you want, copy the file, change the values.

  data/mm/modernmayhem/armor/     armor pieces
  data/mm/modernmayhem/curio/     backpacks, chest rigs, cosmetics
  data/mm/modernmayhem/goggles/   night vision, thermal, visors
  assets/mm/                      the models, textures and animations they point at

IMPORTANT
---------
By default this folder is rewritten every time the game starts, so anything you change here is lost.

To take ownership of it, open

    .minecraft/modernmayhem/modernmayhem.properties

and set

    regenerate_default_pack=false

From then on your edits stick — including deleting items you don't want, or emptying the pack entirely
and shipping only your own.

Packs you create yourself are never touched by this setting, so you don't need to change anything just
to add gear alongside ModernMayhem's.

Full documentation: docs/CONTENT_PACKS.md
