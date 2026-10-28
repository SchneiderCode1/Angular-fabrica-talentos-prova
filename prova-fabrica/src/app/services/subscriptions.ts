import { ProfileModel } from './../pages/landing-page/components/subscription-modal/models/subscription-model';
import { Injectable, computed, inject, signal } from '@angular/core';
import { DOCUMENT } from "@angular/common";

const CHAVE = 'corrida.inscricoes.v1';

@Injectable({
  providedIn: 'root',
})
export class Subscriptions {
  private readonly localStorage = inject(DOCUMENT).defaultView?.localStorage;

  readonly error = signal<string | null>(null);

  readonly list = signal<ProfileModel[]>(this.load());

  readonly subscriptions = this.list.asReadonly();

  readonly total = computed(() => this.list().length);
  readonly total5km = computed(() => this.list().filter(p => p.race_length === 'KM_5').length);
  readonly total10km = computed(() => this.list().filter(p => p.race_length === 'KM_10').length);

  add(profile: ProfileModel): boolean {
    if (this.list().some(p => p.email === profile.email)) {
      this.error.set('Já existe uma inscrição com este e-mail.');
      return false;
    }
    return this.save([...this.list(), profile]);
  }

  load(): ProfileModel[] {
    try {
      const raw = this.localStorage?.getItem(CHAVE);
      return raw ? JSON.parse(raw) : [];
    } catch {
      this.error.set("Os dados salvos estavam corrompidos. A lista foi iniciada vazia.");
      return [];
    }
  }

  private save(profile: ProfileModel[]): boolean {
    try {
      if (!this.localStorage) throw new Error('localStorage indisponível');
      this.localStorage.setItem(CHAVE, JSON.stringify(profile));
      this.list.set(profile);
      this.error.set(null);
      return true;
    } catch {
      this.error.set("Não foi possível salvar. Tente novamente.");
      return false;
    }
  }

  remove(email: string):boolean {
    return this.save(this.list().filter(p => p.email !== email));
  }
  
  clear(): void {
    this.localStorage?.clear();
  }
}
