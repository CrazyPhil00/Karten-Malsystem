<h1> MalSystem </h1>


<h2> Commands: </h2>

Eine Leinwand erstellen:

    /canvas create name x1 y1 z1 x2 y2 z2 width height

    - name: Name der zu erstellenden Leinwand.
    - x1/y1/z1: Anfangs-Coordinaten der Leinwand
    - x2/y2/z2: End-Coordinatedn der Leinwand
    - height/width: Breite/Höhe der Leinwand (müssen die gleichen Werte sein)
    
    Wichtig!: Beim erstellen einer Leinwand wird die Position des ausführenden Spielers als Leinwand-Spawnpunkt benutzt.
    
Eine Leinwand löschen:
    
    /canvas delete name

    - name: Name des zu löschender Leinwand

Leinwände reloaden:

    /canvas reload
    
    Läd alle Leinwände die in der Config gespeichert sind neu

---

NPC erstellen:
    
    /canvas-npc spawn name npc-type
    
    - name: Sichtbarer Name des NPC's (Kann farb codes beinhalten z.B. &c)
    - npc-type: 
            - CREATE-CANVAS: NPC der das Inventar zum erstellen einer Map öffnet
            - SAVE-CANVAS: NPC der das inventart zum Kauf der Map öffnet

NPC löschen:

    /canvas-npc remove
    
    Wichtig!: Löscht alle NPC's des Plugins in einem Radius von 4 Blöcken

---

<h2>Config</h2>

    plugin-prefix: Prefix der vor jeder Nachricht steht die das Plugin sendet (Kann mit farb codes verwendet werden)
    

    npc:
        select-canvas: NPC der das Inventar zum erstellen einer Map öffnet
        save-canvas: NPC der das inventart zum Kauf der Map öffnet
        item-slot: Ist der Item-slot eines Items im NPC Inventar
<img src="https://proxy.spigotmc.org/cb7b065c27a6e19884eb8570db0767c1036836bb?url=https%3A%2F%2Fwiki.vg%2Fimages%2Fthumb%2F1%2F19%2FChest-slots.png%2F300px-Chest-slots.png" width="30%">