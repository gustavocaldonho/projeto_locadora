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
