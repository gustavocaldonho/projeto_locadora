export interface Ator {
  id?: number;
  nome: string;
}

export interface Diretor {
  id?: number;
  nome: string;
}

export interface Classe {
  id?: number;
  nome: string;
  valor: number;
  prazoDevolucao: number;
}

export interface Titulo {
  id?: number;
  nome: string;
  ano: number;
  sinopse?: string;
  categoria: string;
  atores: Ator[];
  diretor: Diretor;
  classe: Classe;
}

export type TipoItem = 'FITA' | 'DVD' | 'BLURAY';

export const TIPOS_ITEM: { valor: TipoItem; descricao: string }[] = [
  { valor: 'FITA', descricao: 'Fita' },
  { valor: 'DVD', descricao: 'DVD' },
  { valor: 'BLURAY', descricao: 'Blu-ray' },
];

export interface Item {
  id?: number;
  numSerie: string;
  dtAquisicao: string; // formato yyyy-MM-dd
  tipoItem: TipoItem;
  titulo: Titulo;
}

/** Corpo de erro devolvido pelo backend (classe ErroPadrao). */
export interface ErroPadrao {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
}

// ---------- Atendimento a Cliente (Sprint 3) ----------

export type Sexo = 'MASCULINO' | 'FEMININO';

export const SEXOS: { valor: Sexo; descricao: string }[] = [
  { valor: 'FEMININO', descricao: 'Feminino' },
  { valor: 'MASCULINO', descricao: 'Masculino' },
];

/** Campos comuns a sócios e dependentes. numInscricao é gerado pelo backend. */
export interface Cliente {
  numInscricao?: number;
  nome: string;
  dtNascimento: string; // yyyy-MM-dd
  sexo: Sexo;
  estahAtivo?: boolean;
  tipo?: 'SOCIO' | 'DEPENDENTE';
}

export interface Socio extends Cliente {
  cpf: string;
  endereco: string;
  tel: string;
}

export interface Dependente extends Cliente {
  socio: Partial<Socio>;
}

export type SituacaoLocacao = 'VIGENTE' | 'ATRASADA' | 'DEVOLVIDA';

export interface Locacao {
  id: number;
  dtLocacao: string;
  dtDevolucaoPrevista: string;
  dtDevolucaoEfetiva?: string;
  valorCobrado: number;
  multaCobrada?: number;
  paga: boolean;
  situacao: SituacaoLocacao;
  item: Item;
  cliente: Cliente;
}

/** Corpo enviado para criar ou alterar uma locação. */
export interface LocacaoRequest {
  clienteId?: number;
  itemId?: number;
  valorCobrado?: number | null;
  dtDevolucaoPrevista?: string | null;
  paga?: boolean;
}

export interface DevolucaoInfo {
  locacao: Locacao;
  diasAtraso: number;
  valorLocacao: number;
  multa: number;
  valorAPagar: number;
}
