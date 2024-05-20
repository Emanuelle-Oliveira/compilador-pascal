global main
extern printf
extern scanf

section .text 
main: 	; Entrada do programa
	push ebp
	mov ebp, esp
	sub esp, 8
	sub esp, 20
	push rotuloString1
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	mov edx, ebp
	lea eax, [edx - 0]
	push eax
	push @Integer
	call scanf
	add esp, 8
	push rotuloString2
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloString3
	call printf
	add esp, 4
	push 10
	push @Integer
	call printf
	add esp, 8
	push rotuloString4
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	push rotuloString5
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4
	push rotuloString6
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
	push rotuloString7
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 1
	pop dword[ebp - 0]
rotuloFOR8: 
push 10
	push ecx
	mov ecx, dword[ebp - 0]
	cmp ecx, dword[esp+4]
	jg rotuloFIMFOR9
	pop ecx
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4
	add dword[ebp - 0], 1
	jmp rotuloFOR8
rotuloFIMFOR9: 	push rotuloString10
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 1
	pop eax
	mov dword[ebp - 0], eax
rotuloRepeat11: 	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4
	push dword[ebp - 0]

push 1
	pop eax
	add dword[ESP], eax
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jle rotuloFalsoREL12
	mov dword [ESP], 1
	jmp rotuloSaidaREL13
rotuloFalsoREL12: 	mov dword [ESP], 0
rotuloSaidaREL13: 	cmp dword[esp], 0

	je rotuloRepeat11
	push rotuloString14
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
rotuloWhile15: 	push dword[ebp - 0]

push 1
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL17
	mov dword [ESP], 1
	jmp rotuloSaidaREL18
rotuloFalsoREL17: 	mov dword [ESP], 0
rotuloSaidaREL18: 	cmp dword[esp], 0

	je rotuloFimWhile16
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4
	push dword[ebp - 0]

push 1
	pop eax
	sub dword[ESP], eax
	pop eax
	mov dword[ebp - 0], eax
	jmp rotuloWhile15
rotuloFimWhile16: 	push rotuloString19
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 10
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL20
	mov dword [ESP], 1
	jmp rotuloSaidaREL21
rotuloFalsoREL20: 	mov dword [ESP], 0
rotuloSaidaREL21: 	cmp dword [esp], 0

	je rotuloElse22
	push rotuloString24
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf23
rotuloElse22:
rotuloFimIf23: 	push rotuloString25
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 1
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL26
	mov dword [ESP], 1
	jmp rotuloSaidaREL27
rotuloFalsoREL26: 	mov dword [ESP], 0
rotuloSaidaREL27: 	cmp dword [esp], 0

	je rotuloElse28
	push rotuloString30
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf29
rotuloElse28:
rotuloFimIf29: 	push rotuloString31
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 20
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL32
	mov dword [ESP], 1
	jmp rotuloSaidaREL33
rotuloFalsoREL32: 	mov dword [ESP], 0
rotuloSaidaREL33: 	cmp dword [esp], 0

	je rotuloElse34
	push rotuloString36
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf35
rotuloElse34:
	push rotuloString37
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
rotuloFimIf35: 	push rotuloString38
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 1
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL39
	mov dword [ESP], 1
	jmp rotuloSaidaREL40
rotuloFalsoREL39: 	mov dword [ESP], 0
rotuloSaidaREL40: 	cmp dword [esp], 0

	je rotuloElse41
	push rotuloString43
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf42
rotuloElse41:
	push rotuloString44
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
rotuloFimIf42: 	push rotuloString45
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 15
	pop eax
	mov dword[ebp - 0], eax

push 5
	pop eax
	mov dword[ebp - 4], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL46
	mov dword [ESP], 1
	jmp rotuloSaidaREL47
rotuloFalsoREL46: 	mov dword [ESP], 0
rotuloSaidaREL47: 	push dword[ebp - 4]

push 10
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL48
	mov dword [ESP], 1
	jmp rotuloSaidaREL49
rotuloFalsoREL48: 	mov dword [ESP], 0
rotuloSaidaREL49: 	cmp dword [ESP + 4], 1
	jne rotuloFalsoMTL51
	pop eax
	cmp dword [ESP], eax
	jne rotuloFalsoMTL51
	mov dword [ESP], 1
	jmp rotuloSaidaMTL50
rotuloFalsoMTL51: 	mov dword [ESP], 0
rotuloSaidaMTL50: 	cmp dword [esp], 0

	je rotuloElse52
	push rotuloString54
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf53
rotuloElse52:
rotuloFimIf53: 	push rotuloString55
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 5
	pop eax
	mov dword[ebp - 0], eax

push 5
	pop eax
	mov dword[ebp - 4], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL56
	mov dword [ESP], 1
	jmp rotuloSaidaREL57
rotuloFalsoREL56: 	mov dword [ESP], 0
rotuloSaidaREL57: 	push dword[ebp - 4]

push 10
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL58
	mov dword [ESP], 1
	jmp rotuloSaidaREL59
rotuloFalsoREL58: 	mov dword [ESP], 0
rotuloSaidaREL59: 	cmp dword [ESP + 4], 1
	jne rotuloFalsoMTL61
	pop eax
	cmp dword [ESP], eax
	jne rotuloFalsoMTL61
	mov dword [ESP], 1
	jmp rotuloSaidaMTL60
rotuloFalsoMTL61: 	mov dword [ESP], 0
rotuloSaidaMTL60: 	cmp dword [esp], 0

	je rotuloElse62
	push rotuloString64
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf63
rotuloElse62:
rotuloFimIf63: 	push rotuloString65
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 15
	pop eax
	mov dword[ebp - 0], eax

push 15
	pop eax
	mov dword[ebp - 4], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL66
	mov dword [ESP], 1
	jmp rotuloSaidaREL67
rotuloFalsoREL66: 	mov dword [ESP], 0
rotuloSaidaREL67: 	push dword[ebp - 4]

push 10
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL68
	mov dword [ESP], 1
	jmp rotuloSaidaREL69
rotuloFalsoREL68: 	mov dword [ESP], 0
rotuloSaidaREL69: 	cmp dword [ESP + 4], 1
	jne rotuloFalsoMTL71
	pop eax
	cmp dword [ESP], eax
	jne rotuloFalsoMTL71
	mov dword [ESP], 1
	jmp rotuloSaidaMTL70
rotuloFalsoMTL71: 	mov dword [ESP], 0
rotuloSaidaMTL70: 	cmp dword [esp], 0

	je rotuloElse72
	push rotuloString74
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf73
rotuloElse72:
rotuloFimIf73: 	push rotuloString75
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 5
	pop eax
	mov dword[ebp - 0], eax

push 15
	pop eax
	mov dword[ebp - 4], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL76
	mov dword [ESP], 1
	jmp rotuloSaidaREL77
rotuloFalsoREL76: 	mov dword [ESP], 0
rotuloSaidaREL77: 	push dword[ebp - 4]

push 10
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL78
	mov dword [ESP], 1
	jmp rotuloSaidaREL79
rotuloFalsoREL78: 	mov dword [ESP], 0
rotuloSaidaREL79: 	cmp dword [ESP + 4], 1
	jne rotuloFalsoMTL81
	pop eax
	cmp dword [ESP], eax
	jne rotuloFalsoMTL81
	mov dword [ESP], 1
	jmp rotuloSaidaMTL80
rotuloFalsoMTL81: 	mov dword [ESP], 0
rotuloSaidaMTL80: 	cmp dword [esp], 0

	je rotuloElse82
	push rotuloString84
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf83
rotuloElse82:
rotuloFimIf83: 	push rotuloString85
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 5
	pop eax
	mov dword[ebp - 0], eax

push 15
	pop eax
	mov dword[ebp - 4], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL86
	mov dword [ESP], 1
	jmp rotuloSaidaREL87
rotuloFalsoREL86: 	mov dword [ESP], 0
rotuloSaidaREL87: 	push dword[ebp - 4]

push 10
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL88
	mov dword [ESP], 1
	jmp rotuloSaidaREL89
rotuloFalsoREL88: 	mov dword [ESP], 0
rotuloSaidaREL89: 	cmp dword [ESP + 4], 1
	je rotuloVerdadeMEL91
	cmp dword [ESP], 1
	je rotuloVerdadeMEL91
	mov dword [ESP + 4], 0
	jmp rotuloSaidaMEL90
rotuloVerdadeMEL91: 	mov dword [ESP + 4], 1
rotuloSaidaMEL90: 	add esp, 4
	cmp dword [esp], 0

	je rotuloElse92
	push rotuloString94
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf93
rotuloElse92:
rotuloFimIf93: 	push rotuloString95
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 5
	pop eax
	mov dword[ebp - 0], eax

push 5
	pop eax
	mov dword[ebp - 4], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL96
	mov dword [ESP], 1
	jmp rotuloSaidaREL97
rotuloFalsoREL96: 	mov dword [ESP], 0
rotuloSaidaREL97: 	push dword[ebp - 4]

push 10
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL98
	mov dword [ESP], 1
	jmp rotuloSaidaREL99
rotuloFalsoREL98: 	mov dword [ESP], 0
rotuloSaidaREL99: 	cmp dword [ESP + 4], 1
	je rotuloVerdadeMEL101
	cmp dword [ESP], 1
	je rotuloVerdadeMEL101
	mov dword [ESP + 4], 0
	jmp rotuloSaidaMEL100
rotuloVerdadeMEL101: 	mov dword [ESP + 4], 1
rotuloSaidaMEL100: 	add esp, 4
	cmp dword [esp], 0

	je rotuloElse102
	push rotuloString104
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf103
rotuloElse102:
rotuloFimIf103: 	push rotuloString105
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 15
	pop eax
	mov dword[ebp - 0], eax

push 15
	pop eax
	mov dword[ebp - 4], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL106
	mov dword [ESP], 1
	jmp rotuloSaidaREL107
rotuloFalsoREL106: 	mov dword [ESP], 0
rotuloSaidaREL107: 	push dword[ebp - 4]

push 10
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL108
	mov dword [ESP], 1
	jmp rotuloSaidaREL109
rotuloFalsoREL108: 	mov dword [ESP], 0
rotuloSaidaREL109: 	cmp dword [ESP + 4], 1
	je rotuloVerdadeMEL111
	cmp dword [ESP], 1
	je rotuloVerdadeMEL111
	mov dword [ESP + 4], 0
	jmp rotuloSaidaMEL110
rotuloVerdadeMEL111: 	mov dword [ESP + 4], 1
rotuloSaidaMEL110: 	add esp, 4
	cmp dword [esp], 0

	je rotuloElse112
	push rotuloString114
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf113
rotuloElse112:
rotuloFimIf113: 	push rotuloString115
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 15
	pop eax
	mov dword[ebp - 0], eax

push 5
	pop eax
	mov dword[ebp - 4], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL116
	mov dword [ESP], 1
	jmp rotuloSaidaREL117
rotuloFalsoREL116: 	mov dword [ESP], 0
rotuloSaidaREL117: 	push dword[ebp - 4]

push 10
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL118
	mov dword [ESP], 1
	jmp rotuloSaidaREL119
rotuloFalsoREL118: 	mov dword [ESP], 0
rotuloSaidaREL119: 	cmp dword [ESP + 4], 1
	je rotuloVerdadeMEL121
	cmp dword [ESP], 1
	je rotuloVerdadeMEL121
	mov dword [ESP + 4], 0
	jmp rotuloSaidaMEL120
rotuloVerdadeMEL121: 	mov dword [ESP + 4], 1
rotuloSaidaMEL120: 	add esp, 4
	cmp dword [esp], 0

	je rotuloElse122
	push rotuloString124
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf123
rotuloElse122:
rotuloFimIf123: 	push rotuloString125
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 15
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL126
	mov dword [ESP], 1
	jmp rotuloSaidaREL127
rotuloFalsoREL126: 	mov dword [ESP], 0
rotuloSaidaREL127: 	cmp dword [ESP], 1
	jne rotuloFalsoFL128
	mov dword [ESP], 0
	jmp rotuloSaidaFL129
rotuloFalsoFL128: 	mov dword [ESP], 1
rotuloSaidaFL129: 	cmp dword [esp], 0

	je rotuloElse130
	push rotuloString132
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf131
rotuloElse130:
rotuloFimIf131: 	push rotuloString133
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 5
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]

push 10
	pop eax
	cmp dword [ESP], eax
	jl rotuloFalsoREL134
	mov dword [ESP], 1
	jmp rotuloSaidaREL135
rotuloFalsoREL134: 	mov dword [ESP], 0
rotuloSaidaREL135: 	cmp dword [ESP], 1
	jne rotuloFalsoFL136
	mov dword [ESP], 0
	jmp rotuloSaidaFL137
rotuloFalsoFL136: 	mov dword [ESP], 1
rotuloSaidaFL137: 	cmp dword [esp], 0

	je rotuloElse138
	push rotuloString140
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4
	jmp rotuloFimIf139
rotuloElse138:
rotuloFimIf139: 	push rotuloString141
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 5
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4

push 5

push 5
	pop eax
	add dword[ESP], eax
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4
	push dword[ebp - 0]

push 5
	pop eax
	add dword[ESP], eax
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4

push 5
	push dword[ebp - 0]
	pop eax
	add dword[ESP], eax
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4

push 10
	pop eax
	mov dword[ebp - 4], eax

push 5
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 4]
	push dword[ebp - 0]
	pop eax
	add dword[ESP], eax
	pop eax
	mov dword[ebp - 0], eax
	push dword[ebp - 0]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4

push 0
	pop eax
	mov dword[ebp - 4], eax
	push dword[ebp - 0]
	push dword[ebp - 0]
	pop eax
	add dword[ESP], eax
	pop eax
	mov dword[ebp - 4], eax
	push dword[ebp - 4]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4
	push rotuloString142
	call printf
	add esp, 4
	push rotuloStringLN
	call printf
	add esp, 4

push 1
	pop eax
	mov dword[ebp - 8], eax

push 1
	pop eax
	mov dword[ebp - 12], eax

push 0
	pop eax
	mov dword[ebp - 16], eax

push 0
	pop eax
	mov dword[ebp - 20], eax
	push rotuloString143
	call printf
	add esp, 4
	mov edx, ebp
	lea eax, [edx - 16]
	push eax
	push @Integer
	call scanf
	add esp, 8
	push dword[ebp - 8]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4
	push dword[ebp - 12]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4

push 2
	pop eax
	mov dword[ebp - 24], eax
rotuloWhile144: 	push dword[ebp - 24]
	push dword[ebp - 16]
	pop eax
	cmp dword [ESP], eax
	jg rotuloFalsoREL146
	mov dword [ESP], 1
	jmp rotuloSaidaREL147
rotuloFalsoREL146: 	mov dword [ESP], 0
rotuloSaidaREL147: 	cmp dword[esp], 0

	je rotuloFimWhile145
	push dword[ebp - 8]
	push dword[ebp - 12]
	pop eax
	add dword[ESP], eax
	pop eax
	mov dword[ebp - 20], eax
	push dword[ebp - 20]
	push @Integer
	call printf
	add esp, 8
	push rotuloStringLN
	call printf
	add esp, 4
	push dword[ebp - 12]
	pop eax
	mov dword[ebp - 8], eax
	push dword[ebp - 20]
	pop eax
	mov dword[ebp - 12], eax
	push dword[ebp - 24]

push 1
	pop eax
	add dword[ESP], eax
	pop eax
	mov dword[ebp - 24], eax
	jmp rotuloWhile144
rotuloFimWhile145: 	leave
	ret

section .data

rotuloString1: db '----------READ------------', 0 
rotuloStringLN: db '', 10, 0
@Integer: db '%d',0
rotuloString2: db '----------WRITE------------', 0 
rotuloString3: db 'texto', 0 
rotuloString4: db '', 0 
rotuloString5: db '----------WRITELN------------', 0 
rotuloString6: db 'texto', 0 
rotuloString7: db '----------FOR------------', 0 
rotuloString10: db '----------REPEAT------------', 0 
rotuloString14: db '----------WHILE------------', 0 
rotuloString19: db '----------IF------------', 0 
rotuloString24: db 'Entrou no if', 0 
rotuloString25: db '----------IF2------------', 0 
rotuloString30: db 'Entrou no if', 0 
rotuloString31: db '----------IF_ELSE1------------', 0 
rotuloString36: db 'Entrou no if', 0 
rotuloString37: db 'Entrou no else', 0 
rotuloString38: db '----------IF_ELSE2------------', 0 
rotuloString43: db 'Entrou no if', 0 
rotuloString44: db 'Entrou no else', 0 
rotuloString45: db '----------IF_COM_AND_1------------', 0 
rotuloString54: db 'Entrou no if', 0 
rotuloString55: db '----------IF_COM_AND_2------------', 0 
rotuloString64: db 'Entrou no if', 0 
rotuloString65: db '----------IF_COM_AND_3------------', 0 
rotuloString74: db 'Entrou no if', 0 
rotuloString75: db '----------IF_COM_AND_4------------', 0 
rotuloString84: db 'Entrou no if', 0 
rotuloString85: db '----------IF_COM_OR_1------------', 0 
rotuloString94: db 'Entrou no if', 0 
rotuloString95: db '----------IF_COM_OR_2------------', 0 
rotuloString104: db 'Entrou no if', 0 
rotuloString105: db '----------IF_COM_OR_3------------', 0 
rotuloString114: db 'Entrou no if', 0 
rotuloString115: db '----------IF_COM_OR_4------------', 0 
rotuloString124: db 'Entrou no if', 0 
rotuloString125: db '----------IF_COM_NOT_1------------', 0 
rotuloString132: db 'Entrou no if', 0 
rotuloString133: db '----------IF_COM_NOT_2------------', 0 
rotuloString140: db 'Entrou no if', 0 
rotuloString141: db '----------ATRIBUICAO------------', 0 
rotuloString142: db '----------FIBONACCI------------', 0 
rotuloString143: db 'Informe a quantidade de numeros que deseja ver da sequencia fibonacci: ', 0 
