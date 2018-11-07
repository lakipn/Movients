/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { MovientsTestModule } from '../../../test.module';
import { TorrentDetailComponent } from 'app/entities/torrent/torrent-detail.component';
import { Torrent } from 'app/shared/model/torrent.model';

describe('Component Tests', () => {
    describe('Torrent Management Detail Component', () => {
        let comp: TorrentDetailComponent;
        let fixture: ComponentFixture<TorrentDetailComponent>;
        const route = ({ data: of({ torrent: new Torrent(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MovientsTestModule],
                declarations: [TorrentDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(TorrentDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(TorrentDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.torrent).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
