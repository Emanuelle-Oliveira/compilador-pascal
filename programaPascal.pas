{ teste comentario  }
program condicional;
var
    x, y: integer;
begin
    read(x);
    read(y);
    { outro
    comentario 
    varias linhas }
    if (x >= 10) then
    begin
        write(x);
    end
    else
    begin
        write(y);
    end;
    { comentario com codigo na frente } x := 10;
    {
        end