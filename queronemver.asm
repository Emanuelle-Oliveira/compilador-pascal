global main
extern printf
extern scanf

section .text 
main: 	; Entrada do programa
	push ebp
	mov ebp, esp
	sub esp, 8
	mov edx, ebp
	lea eax, [edx - 0]
	push eax
	push @Integer
	call scanf
	add esp, 8
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloString1
	call printf
	add esp, 4
	push 10
	push @Integer
	call printf
	add esp, 8
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4
	push rotuloString2
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	push 10
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4

push 1
	pop dword[ebp - 0]
rotuloFOR3: 
push 10
	push ecx
	mov ecx, dword[ebp - 0]
	cmp ecx, dword[esp+4]
	jg rotuloFIMFOR4
	pop ecx
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	add dword[ebp - 0], 1
	jmp rotuloFOR3
rotuloFIMFOR4: 	leave
	ret

section .data

@Integer: db '%d',0
rotuloString1: db 'texto', 0 
rotuloStringLN: db '', 10, 0
rotuloString2: db 'texto', 0 
