import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUtilizer } from '../utilizer.model';

@Component({
  selector: 'jhi-utilizer-detail',
  templateUrl: './utilizer-detail.component.html',
})
export class UtilizerDetailComponent implements OnInit {
  utilizer: IUtilizer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ utilizer }) => {
      this.utilizer = utilizer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
