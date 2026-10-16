import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { CrudService } from './crud-service';
import { Ator, Classe, Diretor, Item, Titulo } from './models';

@Injectable({ providedIn: 'root' })
export class AtorService extends CrudService<Ator> {
  constructor(http: HttpClient) {
    super(http, 'atores');
  }
}

@Injectable({ providedIn: 'root' })
export class DiretorService extends CrudService<Diretor> {
  constructor(http: HttpClient) {
    super(http, 'diretores');
  }
}

@Injectable({ providedIn: 'root' })
export class ClasseService extends CrudService<Classe> {
  constructor(http: HttpClient) {
    super(http, 'classes');
  }
}

@Injectable({ providedIn: 'root' })
export class TituloService extends CrudService<Titulo> {
  constructor(http: HttpClient) {
    super(http, 'titulos');
  }
}

@Injectable({ providedIn: 'root' })
export class ItemService extends CrudService<Item> {
  constructor(http: HttpClient) {
    super(http, 'itens');
  }
}
