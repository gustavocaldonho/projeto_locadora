import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { API_URL } from './api';
import { Ator, TituloConsulta } from './models';

/** Consulta de Títulos: liberada para funcionários e clientes. */
@Injectable({ providedIn: 'root' })
export class ConsultaService {
  private readonly url = `${API_URL}/titulos/consulta`;

  constructor(private http: HttpClient) {}

  consultar(filtros: { nome?: string | null; categoria?: string | null; atorId?: number | null }): Observable<TituloConsulta[]> {
    let params = new HttpParams();
    if (filtros.nome) {
      params = params.set('nome', filtros.nome);
    }
    if (filtros.categoria) {
      params = params.set('categoria', filtros.categoria);
    }
    if (filtros.atorId) {
      params = params.set('atorId', filtros.atorId);
    }
    return this.http.get<TituloConsulta[]>(this.url, { params });
  }

  categorias(): Observable<string[]> {
    return this.http.get<string[]>(`${this.url}/categorias`);
  }

  atores(): Observable<Ator[]> {
    return this.http.get<Ator[]>(`${this.url}/atores`);
  }
}
