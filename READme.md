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

