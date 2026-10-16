import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { API_URL } from './api';

/**
 * Operações de CRUD comuns a todos os cadastros.
 * Cada service concreto (AtorService, ClasseService...) só informa o recurso.
 */
export abstract class CrudService<T extends { id?: number }> {
  protected readonly url: string;

  constructor(protected http: HttpClient, recurso: string) {
    this.url = `${API_URL}/${recurso}`;
  }

  listar(): Observable<T[]> {
    return this.http.get<T[]>(this.url);
  }

  buscar(id: number): Observable<T> {
    return this.http.get<T>(`${this.url}/${id}`);
  }

  salvar(objeto: T): Observable<T> {
    if (objeto.id) {
      return this.http.put<T>(`${this.url}/${objeto.id}`, objeto);
    }
    return this.http.post<T>(this.url, objeto);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
