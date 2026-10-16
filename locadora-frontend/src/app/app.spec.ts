import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';

import { App } from './app';
import { AuthService } from './core/auth-service';
import { SharedModule } from './shared/shared-module';

describe('App', () => {
  beforeEach(async () => {
    sessionStorage.clear();
    await TestBed.configureTestingModule({
      imports: [RouterModule.forRoot([]), SharedModule, MatToolbarModule, MatMenuModule],
      declarations: [App],
      providers: [provideHttpClient()],
    }).compileComponents();
  });

  afterEach(() => sessionStorage.clear());

  it('deve criar o app', () => {
    const fixture = TestBed.createComponent(App);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('não mostra o menu sem login', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    expect((fixture.nativeElement as HTMLElement).querySelector('mat-toolbar')).toBeNull();
  });

  it('funcionário vê o acervo e o atendimento', () => {
    sessionStorage.setItem(
      'locadora-sessao',
      JSON.stringify({ usuario: { login: 'admin', nome: 'Funcionário', perfil: 'FUNCIONARIO' }, credenciais: 'x' }),
    );
    TestBed.inject(AuthService);
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const texto = (fixture.nativeElement as HTMLElement).textContent;
    expect(texto).toContain('Acervo');
    expect(texto).toContain('Locações');
    expect(texto).toContain('Consultar títulos');
  });

  it('cliente só vê a consulta', () => {
    sessionStorage.setItem(
      'locadora-sessao',
      JSON.stringify({ usuario: { login: 'cliente', nome: 'Cliente', perfil: 'CLIENTE' }, credenciais: 'x' }),
    );
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const texto = (fixture.nativeElement as HTMLElement).textContent;
    expect(texto).toContain('Consultar títulos');
    expect(texto).not.toContain('Acervo');
    expect(texto).not.toContain('Locações');
  });
});
