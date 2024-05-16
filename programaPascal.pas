program completo;
var
    x, y: integer;
begin

    writeln('----------READ------------');

    read(x);
  {  read(y);
    read(x, y); }

    writeln('----------WRITE------------');

    write(x);
    write('texto');
    write(10);

    writeln('');
    writeln('----------WRITELN------------');

    writeln(x);
    writeln('texto');
    writeln(10);

    writeln('----------FOR------------');

    for x := 1 to 10 do begin
       writeln(x);
      { write(y); } 
    end;

    writeln('----------REPEAT------------');
    x := 1;
    repeat
        writeln(x);
        x := x + 1;
    until (x > 10);

    writeln('----------WHILE------------');
    while (x >= 1) do
    begin
        writeln(x);
        x := x - 1;
    end;

    writeln('----------IF------------');
    x := 10;
    if (x >= 10) then
    begin
        writeln('Entrou no if');
    end;
    writeln('----------IF2------------');
    x := 1;
    if (x >= 10) then
    begin
        writeln('Entrou no if');
    end;

    writeln('----------IF_ELSE1------------');
    x := 20;
    if (x >= 10) then
    begin
        writeln('Entrou no if');
    end
    else
    begin
        writeln('Entrou no else');
    end;
    writeln('----------IF_ELSE2------------');
    x := 1;
    if (x >= 10) then
    begin
        writeln('Entrou no if');
    end
    else
    begin
        writeln('Entrou no else');
    end;

    writeln('----------IF_COM_AND_1------------');
    x := 15;
    y := 5;
    if (x >= 10 and y <= 10) then
    begin
        writeln('Entrou no if');
    end;
    writeln('----------IF_COM_AND_2------------');
    x := 5;
    y := 5;
    if (x >= 10 and y <= 10) then
    begin
        writeln('Entrou no if');
    end;
    writeln('----------IF_COM_AND_3------------');
    x := 15;
    y := 15;
    if (x >= 10 and y <= 10) then
    begin
        writeln('Entrou no if');
    end;
    writeln('----------IF_COM_AND_4------------');
    x := 5;
    y := 15;
    if (x >= 10 and y <= 10) then
    begin
        writeln('Entrou no if');
    end;

    writeln('----------IF_COM_OR_1------------');
    x := 5;
    y := 15;
    if (x >= 10 or y <= 10) then
    begin
        writeln('Entrou no if');
    end;
    writeln('----------IF_COM_OR_2------------');
    x := 5;
    y := 5;
    if (x >= 10 or y <= 10) then
    begin
        writeln('Entrou no if');
    end;
    writeln('----------IF_COM_OR_3------------');
    x := 15;
    y := 15;
    if (x >= 10 or y <= 10) then
    begin
        writeln('Entrou no if');
    end;
    writeln('----------IF_COM_OR_4------------');
    x := 15;
    y := 5;
    if (x >= 10 or y <= 10) then
    begin
        writeln('Entrou no if');
    end;

    writeln('----------IF_COM_NOT_1------------');
    x := 15;
    if (not (x >= 10)) then
    begin
        writeln('Entrou no if');
    end;
    writeln('----------IF_COM_NOT_2------------');
    x := 5;
    if (not (x >= 10)) then
    begin
        writeln('Entrou no if');
    end;

    writeln('----------ATRIBUICAO------------');
    x := 5;
end.