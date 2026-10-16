import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable, computed, signal } from '@angular/core';
import { Observable, tap } from 'rxjs';

import { API_URL } from './api';
import { Usuario } from './models';

const CHAVE = 'locadora-sessao';

interface Sessao {
  usuario: Usuario;
  /** login:senha em Base64, enviado no cabeçalho Authorization: Basic ... */
  credenciais: string;
}

/**
 * Guarda quem está logado. A sessão fica no sessionStorage do navegador,
 * então some quando a aba é fechada.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly sessao = signal<Sessao | null>(this.lerSessao());

  readonly usuario = computed(() => this.sessao()?.usuario ?? null);
  readonly logado = computed(() => this.sessao() !== null);
  readonly ehFuncionario = computed(() => this.usuario()?.perfil === 'FUNCIONARIO');

  constructor(private http: HttpClient) {}

  /** Confere login e senha no backend; se estiverem certos, abre a sessão. */
  entrar(login: string, senha: string): Observable<Usuario> {
    const credenciais = btoa(unescape(encodeURIComponent(`${login}:${senha}`)));
    const headers = new HttpHeaders({ Authorization: `Basic ${credenciais}` });
    return this.http.get<Usuario>(`${API_URL}/auth/usuario`, { headers }).pipe(
      tap((usuario) => {
        const sessao = { usuario, credenciais };
        sessionStorage.setItem(CHAVE, JSON.stringify(sessao));
        this.sessao.set(sessao);
      }),
    );
  }

  sair(): void {
    sessionStorage.removeItem(CHAVE);
    this.sessao.set(null);
  }

  /** Valor do cabeçalho Authorization, ou null se ninguém estiver logado. */
  cabecalhoAutorizacao(): string | null {
    const sessao = this.sessao();
    return sessao ? `Basic ${sessao.credenciais}` : null;
  }

  private lerSessao(): Sessao | null {
    try {
      const texto = sessionStorage.getItem(CHAVE);
      return texto ? (JSON.parse(texto) as Sessao) : null;
    } catch {
      return null;
    }
  }
}
