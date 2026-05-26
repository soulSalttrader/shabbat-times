### Test Scenario Naming Convention

**Pattern:** ```{DOMAIN}_{FLOW}_S{INDEX}```

#### Examples

- PERM_FRESH_S1
- PERM_SETTINGS_S2
- PERM_RATIONAL_S3

#### Breakdown

|Part|Meaning|Example|
|---|---|---|
|PERM|Domain / Feature|PERM, ONBOARD, MAP, AUTH|
|FRESH|Flow / Scenario group|FRESH, SETTINGS, RESTART, EDGE|
|S1|Sequential index within the flow|S1, S2, S3|

#### Legend

| Symbol | Meaning            |
| ------ | ------------------ |
| 📏     | Unit test          |
| 🖐️    | Manual test        |
| 🎨     | UI / Espresso test |
