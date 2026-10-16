import { TestBed } from '@angular/core/testing';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterModule } from '@angular/router';

import { App } from './app';
import { SharedModule } from './shared/shared-module';

describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RouterModule.forRoot([]), SharedModule, MatToolbarModule, MatMenuModule],
      declarations: [App],
    }).compileComponents();
  });

  it('deve criar o app', () => {
    const fixture = TestBed.createComponent(App);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('deve mostrar o acervo e o atendimento na barra de navegação', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const texto = (fixture.nativeElement as HTMLElement).textContent;
    expect(texto).toContain('Locadora Passatempo');
    expect(texto).toContain('Acervo');
    expect(texto).toContain('Locações');
    expect(texto).toContain('Devolução');
  });
});
