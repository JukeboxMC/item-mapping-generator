# item-mapping-generator

## How to use
* Place the source and target item palette [here](src/main/resources/item_palette)
    * The file names must have the following format: item_palette.major_minor_patch.json, e.g. item_palette.1_20_80.json
* Add the versions as program arguments (1st is source, 2nd is target)
    * The version is used to identify the palette file
* Add all relevant upgrade schema json files
  in [ItemEntryUpdaterBuilder](src/main/kotlin/org/jukeboxmc/mapping/entry/ItemEntryUpdaterBuilder.kt)
    * For example, if you want to create a 1.20.0 -> 1.20.80 mapping, you must include all versions from
      [1.20.0.23_beta.json](src/main/resources/BedrockItemUpgradeSchema/id_meta_upgrade_schema/0121_1.20.0.23_beta_to_1.20.10.24_beta.json)
      to [1.20.80.24_beta.json](src/main/resources/BedrockItemUpgradeSchema/id_meta_upgrade_schema/0181_1.20.70.24_beta_to_1.20.80.24_beta.json)
* Execute the program. The mapping files are created [here](src/main/resources/mappings) if the run was successful

## How to update
* Pull the changes from the BedrockItemUpgradeSchema repository or add the upgrade schemas yourself
* If necessary implement the changes made in the id_meta_upgrade_schema_schema.json file