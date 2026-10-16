import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { API_URL } from './api';
import { Cliente, Dependente, DevolucaoInfo, Item, Locacao, LocacaoRequest, Socio } from './models';

/**
 * Services do Atendimento a Cliente (Sprint 3).
 * Clientes usam numInscricao como identificador, por isso não reaproveitam o CrudService.
 */
@Injectable({ providedIn: 'root' })
export class SocioService {
  private readonly url = `${API_URL}/socios`;

  constructor(private http: HttpClient) {}

  listar(): Observable<Socio[]> {
    return this.http.get<Socio[]>(this.url);
  }

  buscar(id: number): Observable<Socio> {
    return this.http.get<Socio>(`${this.url}/${id}`);
  }

  dependentes(id: number): Observable<Dependente[]> {
    return this.http.get<Dependente[]>(`${this.url}/${id}/dependentes`);
  }

  salvar(socio: Socio): Observable<Socio> {
    if (socio.numInscricao) {
      return this.http.put<Socio>(`${this.url}/${socio.numInscricao}`, socio);
    }
    return this.http.post<Socio>(this.url, socio);
  }

  desativar(id: number): Observable<Socio> {
    return this.http.put<Socio>(`${this.url}/${id}/desativar`, null);
  }

  reativar(id: number): Observable<Socio> {
    return this.http.put<Socio>(`${this.url}/${id}/reativar`, null);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}

@Injectable({ providedIn: 'root' })
export class DependenteService {
  private readonly url = `${API_URL}/dependentes`;

  constructor(private http: HttpClient) {}

  buscar(id: number): Observable<Dependente> {
    return this.http.get<Dependente>(`${this.url}/${id}`);
  }

  salvar(dependente: Dependente): Observable<Dependente> {
    if (dependente.numInscricao) {
      return this.http.put<Dependente>(`${this.url}/${dependente.numInscricao}`, dependente);
    }
    return this.http.post<Dependente>(this.url, dependente);
  }

  desativar(id: number): Observable<Dependente> {
    return this.http.put<Dependente>(`${this.url}/${id}/desativar`, null);
  }

  reativar(id: number): Observable<Dependente> {
    return this.http.put<Dependente>(`${this.url}/${id}/reativar`, null);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}

@Injectable({ providedIn: 'root' })
export class ClienteService {
  constructor(private http: HttpClient) {}

  /** Sócios e dependentes juntos, para escolher o cliente da locação. */
  listar(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${API_URL}/clientes`);
  }
}

@Injectable({ providedIn: 'root' })
export class LocacaoService {
  private readonly url = `${API_URL}/locacoes`;

  constructor(private http: HttpClient) {}

  listar(somenteVigentes: boolean): Observable<Locacao[]> {
    return this.http.get<Locacao[]>(this.url, { params: { vigentes: somenteVigentes } });
  }

  buscar(id: number): Observable<Locacao> {
    return this.http.get<Locacao>(`${this.url}/${id}`);
  }

  itensDisponiveis(): Observable<Item[]> {
    return this.http.get<Item[]>(`${this.url}/itens-disponiveis`);
  }

  criar(dados: LocacaoRequest): Observable<Locacao> {
    return this.http.post<Locacao>(this.url, dados);
  }

  alterar(id: number, dados: LocacaoRequest): Observable<Locacao> {
    return this.http.put<Locacao>(`${this.url}/${id}`, dados);
  }

  cancelar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}

@Injectable({ providedIn: 'root' })
export class DevolucaoService {
  private readonly url = `${API_URL}/devolucoes`;

  constructor(private http: HttpClient) {}

  /** Mostra o que será cobrado, sem registrar a devolução. */
  consultar(numSerie: string): Observable<DevolucaoInfo> {
    return this.http.get<DevolucaoInfo>(`${this.url}/${encodeURIComponent(numSerie)}`);
  }

  devolver(numSerie: string): Observable<DevolucaoInfo> {
    return this.http.post<DevolucaoInfo>(`${this.url}/${encodeURIComponent(numSerie)}`, null);
  }
}
