/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MovientsTestModule } from '../../../test.module';
import { CastDetailComponent } from 'app/entities/cast/cast-detail.component';
import { Cast } from 'app/shared/model/cast.model';

describe('Component Tests', () => {
    describe('Cast Management Detail Component', () => {
        let comp: CastDetailComponent;
        let fixture: ComponentFixture<CastDetailComponent>;
        const route = ({ data: of({ cast: new Cast(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MovientsTestModule],
                declarations: [CastDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CastDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CastDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.cast).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
